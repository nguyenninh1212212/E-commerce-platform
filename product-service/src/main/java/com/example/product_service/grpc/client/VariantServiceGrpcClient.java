package com.example.product_service.grpc.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.ManagedChannel;
import variant.GetVariantsRequest;
import variant.VariantResponse;
import variant.VariantServiceGrpc;

public class VariantServiceGrpcClient {
        private static final Logger log = LoggerFactory.getLogger(VariantServiceGrpcClient.class);
        private final VariantServiceGrpc.VariantServiceBlockingStub blockingStub;

        public VariantServiceGrpcClient(String host, int port) {
                log.info("VariantServiceGrpcClient initialized: host={}, port={}", host, port);
                ManagedChannel channel = io.grpc.ManagedChannelBuilder.forAddress(host, port)
                                .usePlaintext()
                                .build();
                this.blockingStub = VariantServiceGrpc.newBlockingStub(channel);
        }

        public List<VariantResponse> getVariantByProductId(String productId) {
                log.info("Fetching variant by productId: {}", productId);
                try {
                        GetVariantsRequest request = GetVariantsRequest.newBuilder()
                                        .setProductId(productId)
                                        .build();
                        Iterator<VariantResponse> iterator = blockingStub.getVariants(request);
                        List<VariantResponse> response = new ArrayList<>();
                        iterator.forEachRemaining(response::add);
                        return response;
                } catch (Exception e) {
                        return new ArrayList<>();
                }
        }
}
