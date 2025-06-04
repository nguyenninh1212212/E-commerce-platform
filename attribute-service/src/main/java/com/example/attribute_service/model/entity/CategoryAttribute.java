package com.example.attribute_service.model.entity;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.attribute_service.model.dto.Attribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "category_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryAttribute {
    @Id
    private String id;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private List<Attribute> attributes;
}
