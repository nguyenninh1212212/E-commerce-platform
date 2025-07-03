// package com.example.api_gateway.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.cloud.gateway.filter.GatewayFilter;
// import
// org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
// import org.springframework.http.HttpHeaders;
// import org.springframework.stereotype.Component;
// import org.springframework.web.reactive.function.client.WebClient;

// import com.example.api_gateway.excep.UnauthorizedException;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Component
// public class JwtValidationGatewayFilterFactory
// extends AbstractGatewayFilterFactory<Object> {
// private final WebClient webClient;

// public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
// @Value("${auth.service.url}") String baseUrl) {
// super(Object.class);
// this.webClient = webClientBuilder.baseUrl(baseUrl).build();
// }

// @Override
// public GatewayFilter apply(Object config) {
// return (exchange, chain) -> {

// String token =
// exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
// if (token == null || !token.startsWith("Bearer ")) {
// throw new UnauthorizedException(
// token == null ? "Missing Authorization header" : "Invalid Authorization
// header format");
// }
// return webClient.get()
// .uri("/auth/validate")
// .header(HttpHeaders.AUTHORIZATION, token)
// .retrieve()
// .bodyToMono(Void.class)
// .then(chain.filter(exchange));

// };

// }
// }
