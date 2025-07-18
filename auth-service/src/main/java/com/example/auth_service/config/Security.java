package com.example.auth_service.config;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class Security {
        @Value("${uri}")
        private String issuer;

        @Bean
        @Order(1)
        public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
                OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
                http
                                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                                .oidc(Customizer.withDefaults());
                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/**",
                                                                "/auth/**",
                                                                "/v3/api-docs/**", // JSON spec của Swagger
                                                                "/swagger-ui/**", // Tài nguyên UI (CSS, JS)
                                                                "/swagger-ui.html", // HTML chính
                                                                "/webjars/**", // Thư viện JS/CSS
                                                                "/swagger-resources/**" // Tài nguyên bổ sung
                                                ).permitAll())
                                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }

        @Bean
        public RegisteredClientRepository RegisteredClientRepository() {
                RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                                .clientId("gateway-client-id")
                                .clientSecret("dwmORnygbddyMJqbWfm+Jg==")
                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                                .scope("read")
                                .scope("write")
                                .redirectUri("http://localhost:4000/auth/oauth2/code/gateway-client")
                                .scope(OidcScopes.OPENID)
                                .tokenSettings(TokenSettings.builder()
                                                .accessTokenTimeToLive(Duration.ofMinutes(30))
                                                .refreshTokenTimeToLive(Duration.ofHours(1))
                                                .build())
                                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                                .build();

                return new InMemoryRegisteredClientRepository(client);
        }

        @Bean
        public AuthorizationServerSettings authorizationServerSettings() {
                return AuthorizationServerSettings.builder()
                                .issuer(issuer) // cần khớp với issuer-uri bên Resource Server
                                .build();
        }

}
