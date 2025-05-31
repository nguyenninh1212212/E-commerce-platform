package com.example.attribute_service.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.attribute_service.model.dto.res.Attribute;
import com.example.attribute_service.model.dto.res.CategoriesDTO;
import com.example.attribute_service.model.entity.CategoryAttribute;
import com.example.attribute_service.repos.AttributeRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttributeServ {
    private final AttributeRepo attributeRepo;

    private String formatValue(String value, Set<String> existingValueSet) {
        if (value == null || value.trim().isEmpty())
            return "";

        String lower = value.toLowerCase().trim();
        if (existingValueSet != null && existingValueSet.contains(lower)) {
            return lower;
        }

        if (existingValueSet != null)
            existingValueSet.add(lower);

        // Keep full phrase with first word capitalized
        String[] words = lower.split("\\s+");
        words[0] = Character.toUpperCase(words[0].charAt(0)) + words[0].substring(1);
        return String.join(" ", words);
    }

    private CategoryAttribute getCategoryAttribute(String id) {
        return attributeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void addCategory(String category) {
        attributeRepo.save(
                CategoryAttribute.builder()
                        .category(category)
                        .createdAt(Instant.now())
                        .build());
    }

    public void addAttribute(String id, List<Attribute> attributes) {
        CategoryAttribute categoryAttribute = getCategoryAttribute(id);

        List<Attribute> existingAttributes = categoryAttribute.getAttributes();
        if (existingAttributes == null) {
            existingAttributes = new ArrayList<>();
        }

        Set<String> existingValueSet = existingAttributes.stream()
                .flatMap(attr -> attr.getValues().stream())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Map<String, Attribute> merged = new LinkedHashMap<>();
        for (Attribute attr : existingAttributes) {
            merged.put(attr.getName(), attr);
        }

        for (Attribute attr : attributes) {
            List<String> newValues = attr.getValues().stream()
                    .map(value -> formatValue(value, existingValueSet))
                    .toList();

            attr.setValues(newValues);
            merged.put(attr.getName(), attr);
        }

        categoryAttribute.setAttributes(new ArrayList<>(merged.values()));
        categoryAttribute.setCreatedAt(Instant.now());
        attributeRepo.save(categoryAttribute);
    }

    public void deleteAttribute(String id, String attributeName) {
        try {
            CategoryAttribute categoryAttribute = getCategoryAttribute(id);
            List<Attribute> attributes = categoryAttribute.getAttributes();
            if (attributes != null) {
                attributes.removeIf(attr -> attr.getName().equals(attributeName));
                categoryAttribute.setAttributes(attributes);
                categoryAttribute.setUpdatedAt(Instant.now());
                attributeRepo.save(categoryAttribute);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting attribute: " + e.getMessage());
        }
    }

    public void deleteValueByAttribute(String id, String attributeName) {
        try {
            CategoryAttribute categoryAttribute = getCategoryAttribute(id);
            List<Attribute> attributes = categoryAttribute.getAttributes();
            if (attributes != null) {
                attributes.removeIf(attr -> attr.getName().equals(formatValue(attributeName, null)));
                categoryAttribute.setAttributes(attributes);
                categoryAttribute.setUpdatedAt(Instant.now());
                attributeRepo.save(categoryAttribute);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting attribute: " + e.getMessage());
        }
    }

    public void deleteAllAttributesByCategory(String id) {
        CategoryAttribute categoryAttribute = getCategoryAttribute(id);
        categoryAttribute.setAttributes(new ArrayList<>());
        categoryAttribute.setUpdatedAt(Instant.now());
        attributeRepo.save(categoryAttribute);
    }

    public void updateAttribute(String id, Attribute attribute) {
        CategoryAttribute categoryAttribute = getCategoryAttribute(id);
        List<Attribute> attributes = categoryAttribute.getAttributes();

        if (attributes == null) {
            throw new RuntimeException("No attributes found for this category");
        }
        int left = 0;
        int right = attributes.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (attributes.get(mid).getName().equals(attribute.getName())) {
                attributes.set(mid, attribute);
                categoryAttribute.setUpdatedAt(Instant.now());
                attributeRepo.save(categoryAttribute);
                return;
            }
            if (attributes.get(mid).getName().compareTo(attribute.getName()) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        throw new RuntimeException("Attribute not found");
    }

    public void updateAttributeValue(String id, Attribute attribute) {
        CategoryAttribute categoryAttribute = getCategoryAttribute(id);
        List<Attribute> attributes = categoryAttribute.getAttributes();
        if (attributes == null) {
            throw new RuntimeException("No attributes found for this category");
        }
        Set<String> existingValueSet = new HashSet<>();
        attribute.setValues(attributes.stream()
                .filter(attr -> attr.getName().equals(attribute.getName()))
                .flatMap(attr -> attr.getValues().stream())
                .map(value -> formatValue(value, existingValueSet))
                .collect(Collectors.toList()));
        existingValueSet.clear();
        attributeRepo.save(categoryAttribute);

    }

    public List<Attribute> getAttributeByCategoryId(String id) {
        CategoryAttribute categoryAttribute = getCategoryAttribute(id);
        return categoryAttribute.getAttributes();
    }

    public List<CategoryAttribute> getAllCategories() {
        return attributeRepo.findAll();
    }

    public List<CategoriesDTO> getCategories() {
        List<CategoryAttribute> categoryAttribute = attributeRepo.findAll();

        return categoryAttribute.stream()
                .map(cat -> CategoriesDTO.builder()
                        .id(cat.getId())
                        .name(cat.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteCategory(String id) {
        CategoryAttribute categoryAttribute = getCategoryAttribute(id);
        attributeRepo.delete(categoryAttribute);
    }

}
