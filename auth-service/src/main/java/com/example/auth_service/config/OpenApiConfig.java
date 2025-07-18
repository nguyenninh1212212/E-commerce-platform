package com.example.auth_service.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${openApi.uri}")
    private String host;

    @Bean
    public OpenApiCustomizer customOpenAPI() {
        return openApi -> {
            openApi.setServers(List.of(new Server().url(host)));
        };
    }
}
