package com.example.attribute_service.service;

import java.util.List;

import com.example.attribute_service.model.dto.Attribute;
import com.example.attribute_service.model.dto.res.CategoriesDTO;
import com.example.attribute_service.model.dto.res.CategoryAttributesRes;

public interface AttributeServ {
    List<CategoryAttributesRes> getAllCategoryAttributes();

    void addCategory(String category);

    void addAttribute(String id, List<Attribute> attributes);

    void deleteAttribute(String id, String attributeName);

    void deleteValueByAttribute(String id, String attributeName);

    void deleteAllAttributesByCategory(String id);

    void updateAttribute(String id, Attribute attribute);

    List<Attribute> getAttributeByCategoryId(String id);

    List<CategoriesDTO> getCategories();

    void deleteCategory(String id);
}
