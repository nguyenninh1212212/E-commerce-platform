package com.example.product_service.mapper;

import lombok.experimental.UtilityClass;
import variant.VariantUpdateRequest;
import variant.VariantsRequest;

import java.util.List;

import com.example.product_service.model.Attributes;
import com.example.product_service.model.dto.req.variant.VariantReq;
import com.example.product_service.model.dto.req.variant.VariantUpdateReq;
import com.example.product_service.model.enums.VariantsStatus;

@UtilityClass
public class ToProto {
    public shared.Attribute toAttribute(Attributes attributes) {
        return shared.Attribute.newBuilder()
                .setName(attributes.getName())
                .addAllValues(attributes.getValue())
                .build();
    }

    public List<shared.Attribute> toListAttribute(List<Attributes> attributes) {
        return attributes.stream().map(
                ToProto::toAttribute).toList();
    }

    public variant.Status toProtoStatus(VariantsStatus status) {
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

    public variant.VariantsRequest toVariantsRequest(VariantReq var) {
        return VariantsRequest.newBuilder()
                .addAllAttributes(ToProto.toListAttribute(var.getAttributes()))
                .setPrice(var.getPrice())
                .setQuantity(var.getQuantity())
                .setStatus(ToProto.toProtoStatus(var.getStatus()))
                .build();
    }

    public variant.VariantsRequest toVariantsRequest(VariantUpdateReq var) {
        return VariantsRequest.newBuilder()
                .addAllAttributes(ToProto.toListAttribute(var.getAttributes()))
                .setPrice(var.getPrice())
                .setQuantity(var.getQuantity())
                .setStatus(ToProto.toProtoStatus(var.getStatus()))
                .build();
    }

    public variant.VariantUpdateRequest toVariantUpdateRequest(VariantUpdateReq var) {
        return VariantUpdateRequest.newBuilder()
                .setVariantId(var.getVariantId())
                .setVariantsRequest(toVariantsRequest(var))
                .build();
    }
}
