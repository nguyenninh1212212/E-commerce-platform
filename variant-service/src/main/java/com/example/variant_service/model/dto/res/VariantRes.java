package com.example.variant_service.model.dto.res;

import com.example.variant_service.model.Inventory;
import com.example.variant_service.model.dto.VariantBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VariantRes extends VariantBase {
    private String id;
    private String productId;
    private Inventory inventory;
}
