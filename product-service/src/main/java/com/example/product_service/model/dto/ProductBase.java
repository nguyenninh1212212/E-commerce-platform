package com.example.product_service.model.dto;

import java.util.List;

import com.example.product_service.model.Attributes;
import com.example.product_service.model.Variants;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ProductBase {
    private String name;
    private String description;
    private int sales;
    private int inventory;
    private double price;
    private String sellerId;
    private List<Attributes> attributes;
    private List<Variants> variants;
    private List<String> tags;
    private List<String> imageUrl;
}
