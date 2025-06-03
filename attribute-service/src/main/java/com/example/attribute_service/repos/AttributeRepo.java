package com.example.attribute_service.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.attribute_service.model.entity.CategoryAttribute;

public interface AttributeRepo extends MongoRepository<CategoryAttribute, String> {
    CategoryAttribute findByCategory(String category);

    void deleteByCategory(String category);

    boolean existsByCategory(String category);

}
