package com.example.inventory_service.model.dto.res;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InventoryRes {
    private String id;
    private String variantId;
    private int stockTotal;
    private int stockReversed;
    private int stockAvaiable;
    private int lowStockThresold;
    private Instant lastUpdateAt;
}
