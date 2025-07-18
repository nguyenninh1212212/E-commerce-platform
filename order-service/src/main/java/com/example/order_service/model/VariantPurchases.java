package com.example.order_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantPurchases {
    private String variantId;
    private String sku;
    private List<Attributes> attributes;
    private Double price;
}
