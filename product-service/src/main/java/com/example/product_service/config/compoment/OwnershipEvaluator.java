package com.example.product_service.config.compoment;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.product_service.model.entity.Product;

import lombok.RequiredArgsConstructor;

@Component("ownershipEvaluator")
@RequiredArgsConstructor
public class OwnershipEvaluator {
    private final MongoTemplate mongoTemplate;

    public boolean isOwner(String productId, Authentication authentication) {
        String currentUserId = authentication.getName();
        Product product = mongoTemplate.findById(productId, Product.class);
        return product != null && product.getSellerId().equals(currentUserId);
    }

}
