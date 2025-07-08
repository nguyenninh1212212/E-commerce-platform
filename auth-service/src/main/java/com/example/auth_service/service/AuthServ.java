package com.example.auth_service.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.example.auth_service.excep.AlreadyExistException;
import com.example.auth_service.excep.NotFound;
import com.example.auth_service.excep.UnauthorizedException;
import com.example.auth_service.model.dto.req.AuthReq;
import com.example.auth_service.model.dto.res.AuthRes;
import com.example.auth_service.model.entity.Auth;
import com.example.auth_service.model.entity.Role;
import com.example.auth_service.model.enums.ROLE;
import com.example.auth_service.repo.AuthRepo;
import com.example.auth_service.repo.AuthSpeci;
import com.example.auth_service.repo.RoleRepo;
import com.example.auth_service.service.kafka.KafkaProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServ {
    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServ jwtServ;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtDecoder jwtDecoder;
    private final KafkaProducer kafkaProducer;

    public String register(AuthReq req) {
        Specification<Auth> spec = Specification.where(AuthSpeci.hasUsername(req.getUsername()));
        Specification<Auth> spec2 = Specification.where(AuthSpeci.hasEmail(req.getEmail()));

        if (authRepo.findOne(spec).isPresent()) {
            throw new AlreadyExistException("User already exist");
        }
        if (authRepo.findOne(spec2).isPresent()) {
            throw new AlreadyExistException("Email already exist");
        }
        Role userRole = roleRepo.findByName(ROLE.USER).orElseThrow(() -> new NotFound("Role"));

        Auth auth = Auth
                .builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .createdAt(Instant.now())
                .email(req.getEmail())
                .role(List.of(userRole))
                .build();
        authRepo.save(auth);
        kafkaProducer.sendEventProfileCreate(auth.getId().toString(), req.getProfile());

        return "Register successfully!!";

    }

    public AuthRes login(AuthReq req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            Auth auth = (Auth) authentication.getPrincipal();

            String access = jwtServ.accessToken(auth);
            String refresh = jwtServ.refreshToken(auth);
            auth.setRefreshToken(refresh);
            authRepo.save(auth);
            return AuthRes.builder().access(access).refresh(refresh).build();
        } catch (AuthenticationException ex) {
            throw new NotFound("Username not found");
        }

    }

    public String refreshToken(String rawToken) {
        Jwt jwt;

        try {
            jwt = jwtDecoder.decode(rawToken);
        } catch (JwtException ex) {
            throw new UnauthorizedException("Token invalid or expired");
        }

        UUID userId = UUID.fromString(jwt.getSubject());
        if (!jwtServ.isRefreshToken(jwt))
            throw new UnauthorizedException("Token is not refresh");

        Auth auth = authRepo.findById(userId)
                .orElseThrow(() -> new NotFound("User is invalid"));

        return jwtServ.accessToken(auth); // dùng JwtEncoder
    }

}
