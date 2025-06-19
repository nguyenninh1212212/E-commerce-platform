package com.example.variant_service.model.dto;

import java.util.List;

import com.example.variant_service.model.Attribute;
import com.example.variant_service.model.enums.Status;
import com.example.variant_service.validation.add;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VariantBase {

    @NotNull(groups = { add.class })
    private Double price;

    @NotNull(groups = { add.class })
    private Status status;

    @NotNull(groups = { add.class })
    private List<Attribute> attributes;

    @NotNull(groups = { add.class })
    private String sku;

}
