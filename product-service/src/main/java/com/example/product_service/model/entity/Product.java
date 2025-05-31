package com.example.product_service.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.product_service.model.Attributes;

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
    private double price;
    private String description;
    private int sales;
    private int inventory;
    private String brand;
    private String size;
    private String sellerId;
    private int rating;
    private int reviewCount;
    private List<Attributes> attributes;
    private List<String> tags;
    private List<String> imageUrl;
}
