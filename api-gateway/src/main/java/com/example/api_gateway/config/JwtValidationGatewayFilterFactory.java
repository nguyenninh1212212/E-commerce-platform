package com.example.api_gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.api_gateway.excep.UnauthorizedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtValidationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<JwtValidationGatewayFilterFactory.ConfigSwitchFilter> {
    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
            @Value("${auth.service.url}") String baseUrl) {
        super(ConfigSwitchFilter.class);
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public static class ConfigSwitchFilter {
        private boolean enabled = true;
        private List<String> skippedRoutes = new ArrayList<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getSkippedRoutes() {
            return skippedRoutes;
        }

        public void setSkippedRoutes(List<String> skippedRoutes) {
            this.skippedRoutes = skippedRoutes;
        }
    }

    @Override
    public GatewayFilter apply(ConfigSwitchFilter config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath().toString();
            String method = exchange.getRequest().getMethod().name();
            String routeKey = method + ":" + path;

            log.info("Processing request for route: {}", routeKey);
            for (String skip : config.getSkippedRoutes()) {
                log.info("Skipping JWT validation for route: {}", skip.replace("**", ".*"));

                if (routeKey.matches(skip.replace("**", ".*"))) {
                    log.info("Skipping JWT validation for route: {}", skip.replace("**", ".*"));

                    return chain.filter(exchange);
                }
            }

            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (token == null || !token.startsWith("Bearer ")) {
                throw new UnauthorizedException(
                        token == null ? "Missing Authorization header" : "Invalid Authorization header format");
            }
            return webClient.get()
                    .uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .toBodilessEntity()
                    .then(chain.filter(exchange));
        };
    }
}
