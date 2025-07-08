package com.example.product_service.model.dto.req.product;

import java.util.List;

import com.example.product_service.model.dto.ProductBase;
import com.example.product_service.model.dto.req.variant.VariantReq;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProductReq extends ProductBase {
    @NotEmpty(message = "Variants cannot Empty")
    @NotNull(message = "Variants cannot null")
    private List<VariantReq> variants;
}
