package com.example.product_coordinator_service.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

import com.example.product_coordinator_service.model.dto.req.Product;
import com.example.product_coordinator_service.model.dto.req.Variant;

public interface ProductCoordinatorService {
    CompletableFuture<String> CreateProductAndVariants(Product reqProduct, List<Variant> reqVariantList,
            MultipartFile[] file);

    CompletableFuture<String> DeleteProductAndVariantsById(String productId);
}
