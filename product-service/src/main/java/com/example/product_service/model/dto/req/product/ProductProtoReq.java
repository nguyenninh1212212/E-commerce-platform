package com.example.product_service.model.dto.req.product;

import java.util.List;

import com.example.product_service.model.Attributes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductProtoReq {
    private String id;
    private String name;
    private List<Attributes> attribute;
    private double price;
    private int sale;
}
