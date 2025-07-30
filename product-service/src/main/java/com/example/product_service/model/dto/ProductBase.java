package com.example.product_service.model.dto;

import java.util.List;

import com.example.product_service.model.Attributes;
import com.example.product_service.model.Category;
import com.example.product_service.validator.create;

import jakarta.validation.constraints.Max;
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
public class ProductBase {
    @NotBlank(message = "Product name cannot be empty", groups = { create.class })
    private String name;
    @NotBlank(message = "Product name cannot be empty", groups = { create.class })
    private String description;
    @Min(value = 0, message = "Product sale must be greater than zero", groups = { create.class })
    @Max(value = 100, message = "Product sale must be less than 100", groups = { create.class })
    @NotNull(message = "Product sale cannot be null", groups = { create.class })
    private int sales;
    @Min(value = 0, message = "Product price must be greater than zero", groups = { create.class })
    @NotNull(message = "Product price cannot be null", groups = { create.class })
    private double price;
    @NotNull(message = "Product attributes cannot be null", groups = { create.class })
    private List<Attributes> attributes;
    @NotNull(message = "Product tags cannot be null", groups = { create.class })
    private Category tags;
}
