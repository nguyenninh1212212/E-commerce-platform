package com.example.attribute_service.model.dto.res;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryAttributesRes {
    private CategoriesDTO category;
    private List<Attribute> attributes;
}
