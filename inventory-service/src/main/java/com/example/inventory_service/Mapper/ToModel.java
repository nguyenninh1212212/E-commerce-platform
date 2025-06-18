package com.example.inventory_service.Mapper;

import com.example.inventory_service.model.dto.event.VariantCreatedEvent;
import com.example.inventory_service.model.dto.res.InventoryRes;
import com.example.inventory_service.model.entity.Inventory;

import lombok.experimental.UtilityClass;
import variant.event.VariantEvent;

@UtilityClass
public class ToModel {
    public InventoryRes toRes(Inventory inventory) {
        return InventoryRes.builder()
                .lastUpdateAt(inventory.getLastUpdateAt())
                .stockAvaiable(inventory.getStockAvaiable())
                .stockReversed(inventory.getStockReversed())
                .lowStockThresold(inventory.getLowStockThresold())
                .stockTotal(inventory.getStockTotal())
                .id(inventory.getId())
                .variantId(inventory.getVariantId())
                .build();
    }

    public VariantCreatedEvent toVariantEvent(VariantEvent event) {
        return VariantCreatedEvent.builder()
                .quantity(event.getQuantity())
                .variantId(event.getVariantId())
                .build();
    }
}
