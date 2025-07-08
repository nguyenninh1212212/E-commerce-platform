package com.example.product_service.model;

import java.util.List;

import com.example.product_service.validator.create;
import com.example.product_service.validator.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attributes {
    @NotNull(message = "Name cannot be null", groups = { create.class, update.class })
    private String name;
    @NotNull(message = "Value cannot be null", groups = { create.class, update.class })
    @NotEmpty(message = "Value cannot be empty", groups = { create.class, update.class })
    private List<String> value;
}
