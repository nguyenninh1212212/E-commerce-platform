package com.example.variant_service.service.impl;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.example.variant_service.grpc.client.InventoryGrpcClient;
import com.example.variant_service.mapper.ToModel;
import com.example.variant_service.model.dto.event.VariantCreatedEvent;
import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.req.VariantUpdateReq;
import com.example.variant_service.model.dto.res.InventoryUserRes;
import com.example.variant_service.model.dto.res.VariantUserRes;
import com.example.variant_service.model.entity.Variant;
import com.example.variant_service.service.VariantServ;
import com.mongodb.client.result.UpdateResult;
import com.example.variant_service.service.kafka.KafkaProducerVariant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

@RequiredArgsConstructor
public class VariantServImpl implements VariantServ {
    private final MongoTemplate mongoTemplate;
    private final KafkaProducerVariant kafkaProducer;
    private final InventoryGrpcClient inventoryGrpcClient;

    public List<VariantUserRes> findByProductId(String productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(productId));
        List<Variant> variants = mongoTemplate.find(query, Variant.class);
        List<String> variantIds = variants.stream().map(Variant::getId).toList();
        List<InventoryUserRes> inventoryUserReses = inventoryGrpcClient.getUserInventorys(variantIds);
        log.info("inventoryUserReses : ", inventoryUserReses);
        Map<String, InventoryUserRes> inventoryMap = inventoryUserReses.stream()
                .collect(Collectors.toMap(InventoryUserRes::getVariantId, Function.identity()));

        return variants.stream()
                .map(variant -> {
                    InventoryUserRes inventory = Optional.ofNullable(inventoryMap.get(variant.getId()))
                            .orElseThrow(
                                    () -> new RuntimeException("Inventory missing for variantId: " + variant.getId()));

                    return ToModel.toUserResDto(variant, inventory);
                })
                .collect(Collectors.toList());

    }

    @Override
    public void createVariantList(List<VariantReq> variants, String productId) {
        List<Variant> variantList = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<VariantCreatedEvent> createdEvents = new ArrayList<>();
        for (VariantReq variantReq : variants) {
            Variant variant = ToModel.toEntity(variantReq, productId);
            variantList.add(variant);
            quantities.add(variantReq.getQuantity());
        }
        List<Variant> insertedVariants = new ArrayList<>(mongoTemplate.insert(variantList, Variant.class));

        for (int i = 0; i < insertedVariants.size(); i++) {
            Variant variant = insertedVariants.get(i);
            Integer quantity = quantities.get(i);

            VariantCreatedEvent variantCreatedEvent = VariantCreatedEvent
                    .builder()
                    .variantId(variant.getId())
                    .quantity(quantity)
                    .build();

            createdEvents.add(variantCreatedEvent);
        }
        kafkaProducer.sendCreateEvent(createdEvents);
    }

    public void updateVariantPartial(VariantReq req, String id, String productId) {
        Query query = new Query(Criteria.where("id").is(id).and("productId").is(productId));
        Variant variant = mongoTemplate.findOne(query, Variant.class);
        if (variant == null) {
            throw new RuntimeException("Variant not found");
        }

        Update update = new Update();
        update.set("updatedAt", Instant.now());

        List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = req.getClass();
        while (currentClass != null && currentClass != Object.class) {
            allFields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        allFields.forEach(field -> {
            field.setAccessible(true);
            try {
                Object value = field.get(req);
                if (value != null) {
                    update.set(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        });

        if (!update.getUpdateObject().isEmpty()) {
            UpdateResult result = mongoTemplate.updateFirst(query, update, Variant.class);
            log.info("Update matched {} document(s), modified {}", result.getMatchedCount(), result.getModifiedCount());
        }
    }

    public void deleteVariant(String id, String productId) {
        Query query = new Query(Criteria.where("id").is(id).and("productId").is(productId));
        mongoTemplate.remove(query, Variant.class);
    }

    @Override
    public void deleteVariantsByProductId(String productId) {
        Criteria criteria = Criteria.where("productId").is(productId);
        Query query = new Query(criteria);
        try {
            List<String> variantsIds = mongoTemplate.find(query, Variant.class)
                    .stream()
                    .map(Variant::getId)
                    .toList();
            if (!variantsIds.isEmpty()) {
                kafkaProducer.sendDeleteEvent(variantsIds);
                log.info("Gửi sự kiện xóa Kafka thành công, tiến hành xóa DB");

                mongoTemplate.remove(query, Variant.class);
            }
        } catch (Exception e) {
            log.error("Error in deleteVariantsByProductId: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi server khi xóa Variant: " + e.getMessage());
        }
    }

    @Override
    public void updateVariantList(List<VariantUpdateReq> reqList, String productId) {
        Map<String, VariantUpdateReq> updateMap = toUpdateMap(reqList);

        List<Variant> variants = mongoTemplate.find(
                Query.query(Criteria.where("productId").is(productId)),
                Variant.class);

        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Variant.class);

        List<VariantCreatedEvent> updateEvents = new ArrayList<>();

        for (Variant variant : variants) {
            VariantUpdateReq req = updateMap.get(variant.getId());
            if (req == null)
                continue;

            Update update = new Update()
                    .set("price", req.getPrice())
                    .set("status", req.getStatus())
                    .set("attributes", req.getAttributes())
                    .set("updatedAt", Instant.now());

            Query query = Query.query(Criteria.where("id").is(variant.getId()));

            bulkOps.updateOne(query, update);

            updateEvents.add(
                    VariantCreatedEvent.builder()
                            .variantId(req.getId())
                            .quantity(req.getQuantity())
                            .build());
        }
        bulkOps.execute();
        kafkaProducer.sendUpdateEvent(updateEvents);
    }

    private Map<String, VariantUpdateReq> toUpdateMap(List<VariantUpdateReq> reqList) {
        return reqList.stream()
                .collect(Collectors.toMap(
                        VariantUpdateReq::getId,
                        Function.identity(),
                        (v1, v2) -> v2));
    }

    @Override
    public Variant getVariantById(String variantId) {
        Variant variant = mongoTemplate.findById(variantId, Variant.class);
        return variant;
    }

}
