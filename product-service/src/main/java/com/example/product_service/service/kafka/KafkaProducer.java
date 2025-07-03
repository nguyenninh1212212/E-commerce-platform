package com.example.product_service.service.kafka;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.product_service.model.Attributes;
import com.example.product_service.model.dto.req.VariantReq;
import com.example.product_service.model.enums.VariantsStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import variant.VariantsRequest;
import variant.CreateVariantsRequest;
import variant.GetVariantsRequest;
import shared.Attribute;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private static final String VARIANT_TOPIC_CREATE = "variant-create";
    private static final String VARIANT_TOPIC_DELETE = "product-delete";

    private static variant.Status toProtoStatus(VariantsStatus status) {
        if (status == null)
            return variant.Status.UNKNOWN;
        return switch (status) {
            case SOLD_OUT -> variant.Status.SOLD_OUT;
            case IN_STOCK -> variant.Status.IN_STOCK;
            case PRE_ORDER -> variant.Status.PRE_ORDER;
            case DISCONTINUED -> variant.Status.DISCONTINUED;
            default -> variant.Status.UNKNOWN;
        };
    }

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
                .setStatus(toProtoStatus(v.getStatus()))
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

}
