package com.example.variant_service.repos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.variant_service.model.entity.Variant;

@Repository
public interface VariantRepo extends MongoRepository<Variant, String> {
}
