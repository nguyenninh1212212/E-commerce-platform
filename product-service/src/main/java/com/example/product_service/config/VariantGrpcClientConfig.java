package com.example.product_service.config;

import org.springframework.context.annotation.Bean;

import com.example.product_service.grpc.client.VariantServiceGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VariantGrpcClientConfig {
    @Bean
    public VariantServiceGrpcClient variantServiceGrpcClient(
            @Value("${grpc.client.variant-service.address}") String host,
            @Value("${grpc.client.variant-service.port}") int port) {
        return new VariantServiceGrpcClient(host, port);
    }
}
