package com.example.variant_service.model.dto.req;

import com.example.variant_service.model.dto.VariantBase;

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
public class VariantUpdateReq extends VariantBase {
    private String id;

}
