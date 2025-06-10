package com.example.product_service.kafka;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;

import com.example.product_service.model.Attributes;
import com.example.product_service.model.dto.req.ProductProtoReq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import product.events.ProductEvent;
import shared.Attribute;

@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(ProductProtoReq req) {
        List<Attribute> protoAttributes = req.getAttribute().stream()
                .map((Attributes attr) -> Attribute.newBuilder()
                        .setName(attr.getName())
                        .addAllValues(attr.getValue())
                        .build())
                .collect(Collectors.toList());

        ProductEvent event = ProductEvent.newBuilder()
                .setId(req.getId())
                .setPrice(req.getPrice() - (req.getPrice() * (req.getSale() / 100)))
                .addAllAttribute(protoAttributes)
                .setTotal(req.getTotal())
                .setName(req.getName())
                .build();
        try {
            kafkaTemplate.send("product", event.toByteArray());
        } catch (Exception e) {
            log.error("Product kafka : ", e);
        }
    }
}
