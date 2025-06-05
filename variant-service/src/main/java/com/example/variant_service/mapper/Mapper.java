package com.example.variant_service.mapper;

import com.example.variant_service.model.dto.req.VariantReq;
import com.example.variant_service.model.dto.res.VariantRes;
import com.example.variant_service.model.entity.Variant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Mapper {
    public VariantRes toDto(Variant variant) {
        return VariantRes.builder()
                .id(variant.getId())
                .productId(variant.getProductId())
                .attributes(variant.getAttributes())
                .status(variant.getStatus())
                .price(variant.getPrice())
                .quantity(variant.getQuantity())
                .build();
    }

    public Variant toEntity(VariantReq variantReq, String productId) {
        return Variant.builder()
                .productId(productId)
                .attributes(variantReq.getAttributes()) 
                .status(variantReq.getStatus())
                .price(variantReq.getPrice())
                .quantity(variantReq.getQuantity())
                .build();
    }
}
