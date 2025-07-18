package com.example.order_service.grpc.client;

import com.example.order_service.mapper.ToModel;
import com.example.order_service.model.VariantPurchases;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import variant.VariantIdRequest;
import variant.VariantPurchase;
import variant.VariantServiceGrpc.VariantServiceBlockingStub;

@Slf4j
public class VariantGrpcClient {
    @GrpcClient("variant-client")
    private VariantServiceBlockingStub blockingStub;

    public VariantPurchases getVariantPurchases(String vatiantId) {
        try {
            VariantIdRequest variantIdRequest = VariantIdRequest.newBuilder()
                    .setId(vatiantId)
                    .build();
            VariantPurchase variantPurchase = blockingStub.getVariantById(variantIdRequest);
            return ToModel.toRes(variantPurchase);
        } catch (Exception e) {
            log.error("Failed to get sellerId for variant purchase: {}", e);
            throw new RuntimeException("Failed to get sellerId from product-service via gRPC", e);
        }
    }
}
