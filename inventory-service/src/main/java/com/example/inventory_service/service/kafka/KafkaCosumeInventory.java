package com.example.inventory_service.service.kafka;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.example.inventory_service.Mapper.ToModel;

import com.example.inventory_service.model.dto.event.VariantCreatedEvent;
import com.example.inventory_service.service.InventoryService;
import com.google.protobuf.InvalidProtocolBufferException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import variant.event.VariantEventList;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCosumeInventory {
    private final InventoryService inventoryService;

    @KafkaListener(topics = "inventory", groupId = "inventory-service")
    public void consumeEvent(
            ConsumerRecord<String, byte[]> record) {
        byte[] event = record.value();
        try {
            VariantEventList variantEventList = VariantEventList.parseFrom(event);
            log.info("✅ Received variant event list: {}", variantEventList.getVariantEventList());

            List<VariantCreatedEvent> createdEvents = variantEventList.getVariantEventList()
                    .stream()
                    .map(ToModel::toVariantEvent)
                    .toList();

            inventoryService.createInventory(createdEvents);
            createdEvents.forEach(e -> log.info("📦 Variant received: {}", e));

        } catch (InvalidProtocolBufferException e) {
            log.error("❌ Error deserializing VariantEventList: {}", e.getMessage(), e);
        }
    }

}
