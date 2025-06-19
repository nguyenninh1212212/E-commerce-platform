package com.example.variant_service.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUserRes {
    private String variantId;
    private int stockReserved;
    private int stockAvailable;
}