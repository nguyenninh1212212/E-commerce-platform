package com.example.auth_service.service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.auth_service.excep.UnauthorizedException;
import com.example.auth_service.model.entity.Auth;
import com.example.auth_service.repo.AuthRepo;
import com.example.auth_service.repo.AuthSpeci;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServ {

    private final AuthRepo authRepo;

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String SECRET_KEY;
    private final long ACCESS_TOKEN_EXPIRED = 1000 * 60 * 15; // 15 phút
    private final long REFRESH_TOKEN_EXPIRED = 1000 * 60 * 60 * 24 * 7;

    public String accessToken(UserDetails userDetails) {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public String refreshToken(UserDetails userDetails) {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean isRefreshToken(String token) {
        String username = extractUsername(token);
        Specification<Auth> spec = Specification.where(AuthSpeci.hasRefreshToken(token))
                .and(AuthSpeci.hasUsername(username));
        return authRepo.findOne(spec).isPresent();
    }

    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("Token has expired");
            }

        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid JWT");
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpired(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claim) {
        final Claims claims = extractAllClaims(token);
        return claim.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte keys[] = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keys);
    }
}
