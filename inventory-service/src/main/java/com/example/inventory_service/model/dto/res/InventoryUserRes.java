package com.example.inventory_service.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class InventoryUserRes {
    private int stockReversed;
    private int stockAvaiable;
}
