package com.example.auth_service.service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.auth_service.model.entity.Auth;
import com.example.auth_service.model.entity.Role;
import com.example.auth_service.repo.AuthRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServ {

    private final AuthRepo authRepo;

    @Value("${uri}")
    private String uri_issuer;
    private final JwtEncoder jwtEncoder;

    public String accessToken(Auth auth) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(uri_issuer) // hoặc dynamic
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofMinutes(30)))
                .subject(auth.getId().toString()) // đây là userId
                .claim("roles", auth.getRole().stream().map(Role::getName).toList())
                .claim("scope", List.of("read", "write"))
                .build();
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));
        return jwt.getTokenValue();
    }

    public String refreshToken(Auth auth) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(uri_issuer)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofDays(7)))
                .subject(auth.getId().toString())
                .claim("is_refresh", true)
                .claim("scope", List.of("read", "write"))
                .build();
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));
        return jwt.getTokenValue();
    }

    public UUID extractUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    public boolean isRefreshToken(Jwt jwt) {
        return jwt.getClaim("is_refresh");
    }

}
