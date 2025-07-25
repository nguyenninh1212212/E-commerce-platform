package com.example.product_service.model.dto.res;

import com.example.product_service.model.VariantBase;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class VariantRes extends VariantBase {
    private String id;

}
