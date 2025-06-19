package com.example.variant_service.model.dto.req;

import com.example.variant_service.model.dto.VariantBase;
import com.example.variant_service.validation.add;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class VariantReq extends VariantBase {
    @NotNull(groups = { add.class })
    @Min(value = 0, groups = { add.class })
    private Integer quantity;
}
