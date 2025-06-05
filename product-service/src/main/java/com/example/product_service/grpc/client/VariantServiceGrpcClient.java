package com.example.product_service.grpc.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.grpc.ManagedChannel;
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
        private static Logger log = LoggerFactory.getLogger(VariantServiceGrpcClient.class);
        @GrpcClient("variant-client")
        private VariantServiceGrpc.VariantServiceBlockingStub blockingStub;

        public VariantServiceGrpcClient(String host, int port) {
                log.info("VariantServiceGrpcClient initialized: host={}, port={}", host, port);
                ManagedChannel channel = io.grpc.ManagedChannelBuilder.forAddress(host, port)
                                .usePlaintext()
                                .build();
                this.blockingStub = VariantServiceGrpc.newBlockingStub(channel);
        }

        public List<VariantResponse> getVariantByProductId(String productId) {
                try {
                        long start = System.currentTimeMillis();

                        GetVariantsRequest request = GetVariantsRequest.newBuilder()
                                        .setProductId(productId)
                                        .build();
                        Iterator<VariantResponse> iterator = blockingStub.getVariants(request);
                        List<VariantResponse> response = new ArrayList<>();
                        iterator.forEachRemaining(response::add);
                        long end = System.currentTimeMillis();
                        log.info("Time to fetch and collect variants: {} ms", end - start);
                        log.info(productId + "suceesssdadsssssssssss");

                        return response;
                } catch (Exception e) {
                        // return new ArrayList<>();
                        throw new RuntimeException(e.getMessage());
                }
        }
}
