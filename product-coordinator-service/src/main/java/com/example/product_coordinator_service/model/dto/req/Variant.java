package com.example.product_coordinator_service.model.dto.req;

import java.util.List;

import com.example.product_coordinator_service.model.enums.Status;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Variant {
    @NotNull()
    @Min(value = 0)
    private Double price;

    @NotNull()
    @Min(value = 0)
    private Integer quantity;

    @NotNull()
    private Status status;

    @NotNull()
    private List<Attribute> attributes;

    @NotNull()
    private String sku;
}
