package com.example.product_service.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @NotBlank(message = "Category ID cannot be empty")
    private String id;
    @NotBlank(message = "Category name cannot be empty")
    private String name;
}
