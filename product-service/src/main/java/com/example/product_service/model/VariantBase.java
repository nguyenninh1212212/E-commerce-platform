package com.example.product_service.model;

import java.util.List;

import com.example.product_service.model.enums.VariantsStatus;
import com.example.product_service.validator.create;
import com.example.product_service.validator.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VariantBase {
    @NotNull(message = "Attribute cannot be null", groups = { create.class, update.class })
    private List<Attributes> attributes;
    @NotNull(message = "Status cannot be null", groups = { create.class, update.class })
    private VariantsStatus status;
    @NotNull(message = "Pricce cannot be null", groups = { create.class, update.class })
    @Min(value = 0, message = "Price must be at least zero")
    private Double price;
    @NotNull(message = "Pricce cannot be null", groups = { create.class, update.class })
    @Min(value = 0, message = "Price must be at least zero")
    private int quantity;
    private String sku;
}
