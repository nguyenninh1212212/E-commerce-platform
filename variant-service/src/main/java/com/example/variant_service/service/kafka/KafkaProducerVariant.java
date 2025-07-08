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
import variant.event.VariantIdsEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerVariant {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendCreateEvent(List<VariantCreatedEvent> createdEvents) {
        List<VariantEvent> variantEventArrList = createdEvents.stream().map(
                updateEvent -> VariantEvent
                        .newBuilder()
                        .setEventType("INVENTORY_CREATE")
                        .setQuantity(updateEvent.getQuantity())
                        .setVariantId(updateEvent.getVariantId())
                        .build())
                .toList();

        VariantEventList variantEventList = VariantEventList.newBuilder()
                .addAllVariantEvent(variantEventArrList)
                .build();

        try {
            String key = createdEvents.get(0).getVariantId();
            kafkaTemplate.send("inventory-create", key, variantEventList.toByteArray());
            log.info("Sending event to Kafka with {} variants", variantEventArrList.size());
        } catch (Exception e) {
            log.error("Error send event InventoryCreateEvent : {}", e);
        }
    }

    public void sendDeleteEvent(List<String> variantIds) {

        VariantIdsEvent event = VariantIdsEvent.newBuilder()
                .addAllVariantId(variantIds)
                .build();
        try {
            String key = event.getVariantId(0);
            kafkaTemplate.send("variant-delete", key, event.toByteArray());
            log.info("Sending event to Kafka with {} variants", variantIds.size());
        } catch (Exception e) {
            log.error("Error send event InventoryDeleteEvent : {}", e.getMessage());
        }
    }

    public void sendUpdateEvent(List<VariantCreatedEvent> updateEvents) {
        List<VariantEvent> variantEventArrList = updateEvents.stream().map(
                updateEvent -> VariantEvent
                        .newBuilder()
                        .setEventType("INVENTORY_UPDATE")
                        .setQuantity(updateEvent.getQuantity())
                        .setVariantId(updateEvent.getVariantId())
                        .build())
                .toList();
        VariantEventList variantEventList = VariantEventList.newBuilder()
                .addAllVariantEvent(variantEventArrList)
                .build();

        try {
            String key = updateEvents.get(0).getVariantId();
            kafkaTemplate.send("inventory-update", key, variantEventList.toByteArray());
            log.info("Sending event to Kafka with {} variants", variantEventArrList.size());
        } catch (Exception e) {
            log.error("Error send event InventoryCreateEvent : {}", e);
        }
    }

}
