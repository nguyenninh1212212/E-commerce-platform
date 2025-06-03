package com.example.variant_service.model.entity;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.variant_service.model.Attribute;
import com.example.variant_service.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variant {
    @Id
    private String id;
    private String productId;
    private List<Attribute> attributes;
    private Status status;
    private String sku;
    private String barcode;
    private Double price;
    private String imgurl;
    private int quantity;
    private Instant createdAt;
    private Instant updatedAt;

}
