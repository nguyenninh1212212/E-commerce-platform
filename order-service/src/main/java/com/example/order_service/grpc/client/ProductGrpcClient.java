package com.example.order_service.grpc.client;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import product.ProductId;
import product.ProductServiceGrpc.ProductServiceBlockingStub;

@Slf4j
@Component
public class ProductGrpcClient {
    @GrpcClient("product-client")
    private ProductServiceBlockingStub blockingStub;

    public String getSellerId(String productId) {
        try {
            ProductId productIdProto = ProductId.newBuilder().setId(productId).build();
            return blockingStub.getSellerId(productIdProto).getId();
        } catch (Exception e) {
            log.error("Failed to get sellerId for productId: {}", productId, e);
            throw new RuntimeException("Failed to get sellerId from product-service via gRPC", e);
        }
    }
}
