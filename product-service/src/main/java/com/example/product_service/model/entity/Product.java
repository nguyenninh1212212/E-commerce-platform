package com.example.product_service.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.product_service.model.Attributes;
import com.example.product_service.model.Category;
import com.example.product_service.model.enums.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Document(collection = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private ProductStatus status;
    private int sales;
    private double price;
    private String sellerId;
    private int rating;
    private int reviewCount;
    private List<Attributes> attributes;
    private Category tag;
    private List<String> imageUrl;
}
