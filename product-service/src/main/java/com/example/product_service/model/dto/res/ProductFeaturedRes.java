package com.example.product_service.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFeaturedRes {
    private String id;
    private String name;
    private String img;
    private int sales;
    private double price;
}
