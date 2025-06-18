package com.example.variant_service.service.kafka;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.variant_service.model.dto.event.VariantCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import variant.event.VariantEvent;
import variant.event.VariantEventList;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerVariant {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(List<VariantCreatedEvent> createdEvents) {
        List<VariantEvent> variantEventArrList = new ArrayList<>();
        for (VariantCreatedEvent variantCreatedEvent : createdEvents) {
            try {
                VariantEvent event = VariantEvent
                        .newBuilder()
                        .setEventType("VARIANT_CREATE")
                        .setQuantity(variantCreatedEvent.getQuantity())
                        .setVariantId(variantCreatedEvent.getVariantId())
                        .build();
                variantEventArrList.add(event);
            } catch (Exception e) {
                throw new RuntimeException("Error in format event: " + e.getMessage());
            }
        }

        VariantEventList variantEventList = VariantEventList.newBuilder()
                .addAllVariantEvent(variantEventArrList)
                .build();

        try {
            String key = createdEvents.get(0).getVariantId(); 
            kafkaTemplate.send("inventory", key, variantEventList.toByteArray());
            log.info("Sending event to Kafka with {} variants", variantEventArrList.size());
        } catch (Exception e) {
            log.error("Error send event InventoryCreateEvent : {}", e);
        }
    }

}
