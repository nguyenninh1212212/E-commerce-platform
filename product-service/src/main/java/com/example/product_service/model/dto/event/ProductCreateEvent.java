package com.example.product_service.model.dto.event;

import java.util.List;

import com.example.product_service.model.dto.req.variant.VariantReq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateEvent {
    private String productId;
    private String type;
    private List<VariantReq> variants;
}
