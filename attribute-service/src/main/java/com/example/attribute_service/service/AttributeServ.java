package com.example.attribute_service.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.example.attribute_service.model.dto.Attribute;
import com.example.attribute_service.model.dto.res.CategoriesDTO;
import com.example.attribute_service.model.dto.res.CategoryAttributesRes;
import com.example.attribute_service.model.entity.CategoryAttribute;
import com.example.attribute_service.repos.AttributeRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttributeServ {

    private final AttributeRepo attributeRepo;
    private final MongoTemplate mongoTemplate;

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

    private String toFormatValue(String value) {
        if (value == null || value.trim().isEmpty())
            throw new RuntimeException("Value cannot be empty");
        String lower = value.toLowerCase().trim();
        String[] words = lower.split("\\s+");
        words[0] = Character.toUpperCase(words[0].charAt(0)) + words[0].substring(1);
        return String.join(" ", words);
    }

    private CategoryAttribute getCategoryAttribute(String id) {
        return attributeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public List<CategoryAttributesRes> getAllCategoryAttributes() {
        List<CategoryAttribute> categoryAttributes = attributeRepo.findAll();
        List<CategoryAttributesRes> categoryAttributesRes = categoryAttributes.stream()
                .map(cat -> CategoryAttributesRes.builder()
                        .category(CategoriesDTO.builder()
                                .id(cat.getId())
                                .name(cat.getCategory())
                                .build())
                        .attributes(cat.getAttributes() != null ? cat.getAttributes() : new ArrayList<>())
                        .build())
                .collect(Collectors.toList());
        return categoryAttributesRes;
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
            categoryAttribute.setAttributes(existingAttributes);
        }

        Set<String> existingNames = existingAttributes.stream()
                .map(attr -> toFormatValue(attr.getName()))
                .collect(Collectors.toSet());

        for (Attribute attr : attributes) {
            if (attr.getValues() == null)
                continue;

            String formattedName = toFormatValue(attr.getName());
            if (formattedName == null || formattedName.isEmpty()) {
                throw new RuntimeException("Attribute name cannot be empty");
            }

            if (existingNames.contains(formattedName))
                continue;

            attr.setName(formattedName);
            attr.setId(UUID.randomUUID().toString());

            Set<String> formattedValues = attr.getValues().stream()
                    .map(str -> toFormatValue(str))
                    .filter(Objects::nonNull)
                    .map(this::toFormatValue)
                    .filter(val -> !val.isEmpty())
                    .distinct()
                    .collect(Collectors.toSet());

            attr.setValues(formattedValues);
            existingAttributes.add(attr);
            existingNames.add(formattedName);
        }

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

    public List<Attribute> getAttributeByCategoryId(String id) {
        CategoryAttribute categoryAttribute = getCategoryAttribute(id);
        return categoryAttribute.getAttributes();
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
