package com.example.variant_service.mapper;

import com.example.variant_service.model.enums.Status;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ToProto {
    public variant.Status toProtoStatus(Status status) {
        if (status == null)
            return variant.Status.UNKNOWN;
        return switch (status) {
            case SOLD_OUT -> variant.Status.SOLD_OUT;
            case IN_STOCK -> variant.Status.IN_STOCK;
            case PRE_ORDER -> variant.Status.PRE_ORDER;
            case DISCONTINUED -> variant.Status.DISCONTINUED;
            default -> variant.Status.UNKNOWN;
        };
    }

}
