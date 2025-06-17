package com.example.variant_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {
    private String id;
    private int stockTotal;
    private int stockReversed;
    private int stockAvaiable;
    private int lowStockThresold;
    private String lastUpdateAt;
}
