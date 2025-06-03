package com.example.product_service.model.dto.res;

import java.util.List;

import com.example.product_service.model.Variants;
import com.example.product_service.model.dto.ProductBase;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ProductRes extends ProductBase {
    private String id;
    private double discount;
    private int rating;
    private int reviewCount;
    private List<Variants> variants;
}
