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
                .sku(variant.getSku())
                .barcode(variant.getBarcode())
                .price(variant.getPrice())
                .imgurl(variant.getImgurl())
                .quantity(variant.getQuantity())
                .build();
    }

    public Variant toEntity(VariantReq variantReq, String productId) {
        return Variant.builder()
                .productId(productId)
                .attributes(variantReq.getAttributes())
                .status(variantReq.getStatus())
                .sku(variantReq.getSku())
                .barcode(variantReq.getBarcode())
                .price(variantReq.getPrice())
                .imgurl(variantReq.getImgurl())
                .quantity(variantReq.getQuantity())
                .build();
    }
}
