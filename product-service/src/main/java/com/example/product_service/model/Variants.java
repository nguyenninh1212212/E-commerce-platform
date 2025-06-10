package com.example.product_service.model;

import java.util.List;

import com.example.product_service.model.enums.VariantsStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variants {
    private String id;
    private String productId;
    private List<Attributes> attributes;
    private VariantsStatus status;
    private Double price;
    private int quantity;
}
