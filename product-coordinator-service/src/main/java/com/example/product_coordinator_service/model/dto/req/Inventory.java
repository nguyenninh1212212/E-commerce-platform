package com.example.product_coordinator_service.model.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Inventory {
    @NotNull
    @Min(value = 0)
    private int quantity;
    @NotBlank
    private String variantId;
}
