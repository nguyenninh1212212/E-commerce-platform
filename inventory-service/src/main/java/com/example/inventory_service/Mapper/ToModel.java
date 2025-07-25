package com.example.inventory_service.Mapper;

import com.example.inventory_service.model.dto.event.VariantCreatedEvent;
import com.example.inventory_service.model.dto.res.InventoryRes;
import com.example.inventory_service.model.dto.res.InventoryUserRes;
import com.example.inventory_service.model.entity.Inventory;

import inventory.InventoryResponse;
import inventory.InventoryUserView;
import lombok.experimental.UtilityClass;
import variant.event.VariantEvent;

@UtilityClass
public class ToModel {
    public InventoryRes toRes(Inventory inventory) {
        return InventoryRes.builder()
                .lastUpdateAt(inventory.getLastUpdateAt())
                .stockAvaiable(inventory.getStockAvaiable())
                .stockReversed(inventory.getStockReversed())
                .id(inventory.getId())
                .variantId(inventory.getVariantId())
                .build();
    }

    public InventoryUserRes toUserViewRes(Inventory inventory) {
        return InventoryUserRes.builder()
                .stockAvaiable(inventory.getStockAvaiable())
                .stockReversed(inventory.getStockReversed())
                .build();
    }

    public VariantCreatedEvent toVariantEvent(VariantEvent event) {
        return VariantCreatedEvent.builder()
                .quantity(event.getQuantity())
                .variantId(event.getVariantId())
                .build();
    }

    public InventoryResponse toInventoryProtoResponse(Inventory ivt) {
        return InventoryResponse.newBuilder()
                .setId(ivt.getId())
                .setVariantId(ivt.getVariantId())
                .setStockReserved(ivt.getStockReversed())
                .setStockAvailable(ivt.getStockAvaiable())
                .setLastUpdatedAt(ivt.getLastUpdateAt().toString())
                .build();
    }

    public InventoryResponse toInventoryProtoResponse(InventoryRes res) {
        return InventoryResponse.newBuilder()
                .setId(res.getId())
                .setVariantId(res.getVariantId())
                .setStockTotal(res.getStockTotal())
                .setStockReserved(res.getStockReversed())
                .setStockAvailable(res.getStockAvaiable())
                .setLowStockThreshold(res.getLowStockThresold())
                .setLastUpdatedAt(res.getLastUpdateAt().toString())
                .build();
    }

    public InventoryUserView toInventoryUserProtoResponse(Inventory ivt) {
        return InventoryUserView.newBuilder()
                .setVariantId(ivt.getVariantId())
                .setStockReserved(ivt.getStockReversed())
                .setStockAvailable(ivt.getStockAvaiable())
                .build();
    }

    public InventoryUserView toInventoryUserProtoResponse(InventoryRes ivt) {
        return InventoryUserView.newBuilder()
                .setVariantId(ivt.getVariantId())
                .setStockReserved(ivt.getStockReversed())
                .setStockAvailable(ivt.getStockAvaiable())
                .build();
    }

}
