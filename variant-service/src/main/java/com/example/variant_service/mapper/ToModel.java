package com.example.variant_service.mapper;

import com.example.variant_service.model.Inventory;
import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.InventoryUserRes;
import com.example.variant_service.model.dto.res.VariantRes;
import com.example.variant_service.model.dto.res.VariantUserRes;
import com.example.variant_service.model.entity.Variant;
import com.example.variant_service.model.enums.Status;

import inventory.InventoryUserView;
import lombok.experimental.UtilityClass;
import variant.VariantsRequest;

@UtilityClass
public class ToModel {

    public Status toStatus(variant.Status status) {
        return switch (status) {
            case SOLD_OUT -> Status.SOLD_OUT;
            case IN_STOCK -> Status.IN_STOCK;
            case PRE_ORDER -> Status.PRE_ORDER;
            case DISCONTINUED -> Status.DISCONTINUED;
            default -> Status.UNKNOWN;
        };
    }

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

    public VariantRes.VariantResBuilder baseVariantBuilder(Variant variant) {
        return VariantRes.builder()
                .id(variant.getId())
                .attributes(variant.getAttributes())
                .status(variant.getStatus())
                .sku(variant.getSku())
                .price(variant.getPrice());
    }

    public VariantUserRes.VariantUserResBuilder baseVariantUserBuilder(Variant variant) {
        return VariantUserRes.builder()
                .id(variant.getId())
                .attributes(variant.getAttributes())
                .status(variant.getStatus())
                .sku(variant.getSku())
                .price(variant.getPrice());
    }
    // --------- VariantsRequest Mapping ---------

    public VariantReq toVariantsReq(VariantsRequest variants) {
        return VariantReq.builder().price(variants.getPrice())
                .quantity(variants.getQuantity())
                .sku(variants.getSku())
                .status(toStatus(variants.getStatus()))
                .attributes(variants.getAttributesList().stream()
                        .map(attr -> new com.example.variant_service.model.Attribute(attr.getName(),
                                attr.getValuesList()))
                        .toList())
                .build();
    }

}
