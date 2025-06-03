package com.example.variant_service.service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.variant_service.mapper.Mapper;
import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.VariantRes;
import com.example.variant_service.model.entity.Variant;
import com.mongodb.client.result.UpdateResult;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class VariantServ {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<VariantRes> findByProductIdWithAttributes(String productId, Map<String, Object> attrs) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(productId));
        if (attrs != null) {
            attrs.forEach((k, v) -> {
                query.addCriteria(Criteria.where("variantAttributes." + k).is(v));
            });
        }
        List<Variant> variants = mongoTemplate.find(query, Variant.class);
        return variants.stream()
                .map(Mapper::toDto)
                .toList();
    }

    public List<VariantRes> findByProductId(String productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(productId));
        List<Variant> variants = mongoTemplate.find(query, Variant.class);
        return variants.stream()
                .map(Mapper::toDto)
                .toList();
    }

    public void createVariant(VariantReq variant, String productId) {
        mongoTemplate.save(Mapper.toEntity(variant, productId));
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
}
