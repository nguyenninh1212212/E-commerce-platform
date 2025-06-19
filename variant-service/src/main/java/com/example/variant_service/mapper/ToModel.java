package com.example.variant_service.mapper;

import java.util.List;

import com.example.variant_service.model.Inventory;
import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.InventoryUserRes;
import com.example.variant_service.model.dto.res.VariantRes;
import com.example.variant_service.model.dto.res.VariantUserRes;
import com.example.variant_service.model.entity.Variant;

import inventory.InventoryUserView;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ToModel {

    // --------- Variant Mapping ---------

    public VariantRes toResDto(Variant variant, Inventory inventories) {
        return baseVariantBuilder(variant)
                .inventory(inventories)
                .build();
    }

    public VariantUserRes toUserResDto(Variant variant, InventoryUserRes inventories) {
        return baseVariantUserBuilder(variant)
                .inventory(inventories)
                .build();
    }

    // --------- Entity Mapping ---------

    public Variant toEntity(VariantReq variantReq, String productId) {
        return Variant.builder()
                .productId(productId)
                .attributes(variantReq.getAttributes())
                .status(variantReq.getStatus())
                .price(variantReq.getPrice())
                .sku(variantReq.getSku())
                .build();
    }

    public InventoryUserRes toUserInventoryRes(InventoryUserView res) {
        return InventoryUserRes.builder()
                .stockAvailable(res.getStockAvailable())
                .stockReserved(res.getStockReserved())
                .variantId(res.getVariantId())
                .build();
    }

    // --------- Shared Builder Logic (private) ---------

    private VariantRes.VariantResBuilder baseVariantBuilder(Variant variant) {
        return VariantRes.builder()
                .id(variant.getId())
                .attributes(variant.getAttributes())
                .status(variant.getStatus())
                .sku(variant.getSku())
                .price(variant.getPrice());
    }

    private VariantUserRes.VariantUserResBuilder baseVariantUserBuilder(Variant variant) {
        return VariantUserRes.builder()
                .id(variant.getId())
                .attributes(variant.getAttributes())
                .status(variant.getStatus())
                .sku(variant.getSku())
                .price(variant.getPrice());
    }
}
