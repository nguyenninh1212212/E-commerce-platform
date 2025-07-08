package com.example.product_service.model.dto.req.variant;

import com.example.product_service.model.VariantBase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class VariantUpdateReq extends VariantBase {
    private String variantId;

}
