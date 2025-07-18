package com.example.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPurchase {
    private String productId;
    private String productName;
    private VariantPurchases variantPurchases;
}
