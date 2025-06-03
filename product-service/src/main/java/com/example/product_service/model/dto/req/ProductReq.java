package com.example.product_service.model.dto.req;

import com.example.product_service.model.dto.ProductBase;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ProductReq extends ProductBase {
    private double price;
    private String sellerId;
}
