package com.example.product_service.service.kafka;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.product_service.mapper.ToProto;
import com.example.product_service.model.Attributes;
import com.example.product_service.model.dto.req.variant.VariantReq;
import com.example.product_service.model.dto.req.variant.VariantUpdateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import variant.VariantsRequest;
import variant.CreateVariantsRequest;
import variant.GetVariantsRequest;
import variant.UpdateVariantsRequest;
import variant.VariantUpdateRequest;
import shared.Attribute;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private static final String VARIANT_TOPIC_CREATE = "variant-create";
    private static final String VARIANT_TOPIC_DELETE = "product-delete";
    private static final String VARIANT_TOPIC_UPDATE = "product-update";

    private List<Attribute> toProtoAttributes(List<Attributes> attrs) {
        return attrs.stream()
                .map(attr -> Attribute.newBuilder()
                        .setName(attr.getName())
                        .addAllValues(attr.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private VariantsRequest toProtoVariant(VariantReq v) {
        List<Attribute> attributes = toProtoAttributes(v.getAttributes());

        return VariantsRequest.newBuilder()
                .setSku(v.getSku())
                .setStatus(ToProto.toProtoStatus(v.getStatus()))
                .setPrice(v.getPrice())
                .setQuantity(v.getQuantity())
                .addAllAttributes(attributes)
                .build();
    }

    public void sendEvent(String productId, List<VariantReq> variantReq) {
        List<VariantsRequest> variants = variantReq.stream()
                .map(this::toProtoVariant)
                .collect(Collectors.toList());

        CreateVariantsRequest event = CreateVariantsRequest
                .newBuilder()
                .setProductId(productId)
                .addAllVariants(variants)
                .build();

        try {
            kafkaTemplate.send(VARIANT_TOPIC_CREATE, productId, event.toByteArray());
            log.info("Sent product [{}] with [{}] variants to Kafka", productId, variants.size());
        } catch (Exception e) {
            log.error("Failed to send product [{}] to Kafka", productId, e);
        }
    }

    public void sendDeleteEvent(String productId) {
        GetVariantsRequest request = GetVariantsRequest.newBuilder()
                .setProductId(productId)
                .build();
        try {
            kafkaTemplate.send(VARIANT_TOPIC_DELETE, productId, request.toByteArray());
            log.info("Sent product delete [{}] with [{}] variants to Kafka", productId);
        } catch (Exception e) {
            log.error("Failed to send product [{}] to Kafka", productId, e);
        }
    }

    public void sendUpdateEvent(String productId, List<VariantUpdateReq> list) {
        List<VariantUpdateRequest> listUdpate = list.stream().map(
                ToProto::toVariantUpdateRequest).toList();
        try {
            UpdateVariantsRequest event = UpdateVariantsRequest.newBuilder()
                    .addAllVariants(listUdpate)
                    .setProductId(productId)
                    .build();
            kafkaTemplate.send(VARIANT_TOPIC_UPDATE, productId, event.toByteArray());
            log.info("Sent product delete [{}] with [{}] variants to Kafka", productId);
        } catch (Exception e) {
            log.error("Failed to send product [{}] to Kafka", productId, e);
        }
    }

}
