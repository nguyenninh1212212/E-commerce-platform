package com.example.product_service.grpc.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import variant.GetVariantsRequest;
import variant.VariantResponse;
import variant.VariantServiceGrpc;

@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class VariantServiceGrpcClient {
        @GrpcClient("variant-client")
        private VariantServiceGrpc.VariantServiceBlockingStub blockingStub;

        public List<VariantResponse> getVariantByProductId(String productId) {
                try {

                        GetVariantsRequest request = GetVariantsRequest.newBuilder()
                                        .setProductId(productId)
                                        .build();
                        Iterator<VariantResponse> iterator = blockingStub.getVariants(request);
                        List<VariantResponse> response = new ArrayList<>();
                        iterator.forEachRemaining(response::add);
                        return response;
                } catch (Exception e) {
                        log.error("Variant grpc error : {}", e.getMessage());
                        throw new RuntimeException("Variant grpc error : " + e.getMessage());
                }
        }
}
