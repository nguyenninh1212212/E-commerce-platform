package com.example.product_coordinator_service.model.dto.req;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull()
    @Min(value = 0)
    private int sales;

    @NotNull()
    @Min(value = 0)
    private int inventory;

    @NotNull()
    @Min(value = 0)
    private double price;

    @NotBlank
    private String sellerId;

    @NotNull()
    private List<Attribute> attributes;

    @NotNull()
    private Category tags;
}
