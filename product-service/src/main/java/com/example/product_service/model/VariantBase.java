package com.example.product_service.model;

import java.util.List;

import com.example.product_service.model.enums.VariantsStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VariantBase {
    private List<Attributes> attributes;
    private VariantsStatus status;
    private Double price;
    private int quantity;
    private String sku;
}
