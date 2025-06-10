package com.example.product_coordinator_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.product_coordinator_service.grpc.client.ProductServiceGrpcClient;
import com.example.product_coordinator_service.grpc.client.VariantServiceGrpcClient;
import com.example.product_coordinator_service.model.dto.req.Product;
import com.example.product_coordinator_service.model.dto.req.Variant;
import com.example.product_coordinator_service.service.ProductCoordinatorService;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class ProductCoordinatorServImpl implements ProductCoordinatorService {
    private final ProductServiceGrpcClient productServiceGrpcClient;
    private final VariantServiceGrpcClient variantServiceGrpcClient;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public CompletableFuture<String> CreateProductAndVariants(Product reqProduct, List<Variant> reqVariantList,
            MultipartFile file) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                return file.getBytes();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor)
                .thenCompose(fileBytes -> CompletableFuture
                        .supplyAsync(() -> productServiceGrpcClient.CreateProduct(reqProduct, fileBytes), executor))
                .thenCompose(productId -> CompletableFuture.supplyAsync(
                        () -> variantServiceGrpcClient.CreateVariant(productId, reqVariantList), executor));
    }

}
