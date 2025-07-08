package com.example.inventory_service.service.kafka;

import java.util.List;
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
import variant.event.VariantIdsEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCosumeInventory {
    private final InventoryService inventoryService;

    @KafkaListener(topics = "inventory-create")
    public void consumeCreateEvent(
            ConsumerRecord<String, byte[]> record) {
        byte[] event = record.value();
        try {
            VariantEventList variantEventList = VariantEventList.parseFrom(event);
            List<VariantCreatedEvent> createdEvents = variantEventList.getVariantEventList()
                    .stream()
                    .map(ToModel::toVariantEvent)
                    .toList();

            inventoryService.createInventory(createdEvents);
        } catch (InvalidProtocolBufferException e) {
            log.error("❌ Error deserializing VariantEventList: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "variant-delete")
    public void consumeDeleteEvent(ConsumerRecord<String, byte[]> record) {
        byte[] event = record.value();
        try {
            VariantIdsEvent variantIdsEvent = VariantIdsEvent.parseFrom(event);
            List<String> variantIds = variantIdsEvent.getVariantIdList()
                    .stream()
                    .collect(Collectors.toList());
            inventoryService.deleteInventorys(variantIds);
        } catch (InvalidProtocolBufferException e) {
            log.error("❌ Error deserializing VariantEventList: {}", e.getMessage(), e);

        }
    }

    @KafkaListener(topics = "inventory-update")
    public void consumeUpdateEvent(
            ConsumerRecord<String, byte[]> record) {
        byte[] event = record.value();
        try {
            VariantEventList variantEventList = VariantEventList.parseFrom(event);
            List<VariantCreatedEvent> createdEvents = variantEventList.getVariantEventList()
                    .stream()
                    .map(ToModel::toVariantEvent)
                    .toList();
            inventoryService.updateInventoryList(createdEvents);
        } catch (InvalidProtocolBufferException e) {
            log.error("❌ Error deserializing VariantEventList: {}", e.getMessage(), e);
        }
    }
}
