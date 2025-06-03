package com.example.product_service.model.dto;

import java.util.List;

import org.checkerframework.checker.units.qual.A;

import com.example.product_service.model.Attributes;
import com.example.product_service.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductBase {
    private String name;
    private String description;
    private int sales;
    private int inventory;
    private double price;
    private String sellerId;
    private List<Attributes> attributes;
    private Category tags;
    private List<String> imageUrl;
}
