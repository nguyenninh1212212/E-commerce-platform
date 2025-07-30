package com.example.product_service.config.compoment;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.product_service.grpc.client.VendorServiceGrpcClient;
import com.example.product_service.model.dto.res.Vendor;
import com.example.product_service.model.entity.Product;

import lombok.RequiredArgsConstructor;

@Component("ownershipEvaluator")
@RequiredArgsConstructor
public class OwnershipEvaluator {
    private final MongoTemplate mongoTemplate;
    private final VendorServiceGrpcClient client;

    public boolean isOwner(String productId, Authentication authentication) {
        String currentUserId = authentication.getName();
        Vendor vendor = client.getVendor();
        Product product = mongoTemplate.findById(productId, Product.class);
        return product != null && product.getVendorId().equals(vendor.getVendorId());
    }

}
