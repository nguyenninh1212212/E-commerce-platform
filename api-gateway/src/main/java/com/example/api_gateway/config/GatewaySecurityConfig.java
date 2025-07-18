package com.example.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

        @Bean
        public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
                return http
                                .csrf(csrf -> csrf.disable())
                                .authorizeExchange(exchange -> exchange
                                                .pathMatchers(
                                                                "/auth/**",
                                                                "/login/**",
                                                                "/swagger/**", // 👈 Cho phép Swagger
                                                                "/swagger-ui/**", // optional nếu bạn dùng trực tiếp
                                                                "/products", // gateway Swagger
                                                                "/v3/api-docs/**" // nếu các service expose OpenAPI qua
                                                                                  // gateway
                                                ).permitAll()
                                                .anyExchange().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                                .build();
        }
}
