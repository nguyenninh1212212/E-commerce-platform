package com.example.attribute_service.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.attribute_service.model.entity.CategoryAttribute;

public interface AttributeRepo extends MongoRepository<CategoryAttribute, String> {
    // Tìm kiếm theo category
    CategoryAttribute findByCategory(String category);

    // Xóa theo category
    void deleteByCategory(String category);

    // Kiểm tra xem category đã tồn tại hay chưa
    boolean existsByCategory(String category);

}
