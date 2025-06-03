package com.example.product_service.model.dto.res;

import com.example.product_service.model.dto.ProductBase;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ProductRes extends ProductBase {
    private String id;
    private int rating;
    private int reviewCount;

}
