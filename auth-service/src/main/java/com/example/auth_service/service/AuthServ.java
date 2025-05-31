package com.example.auth_service.service;

import java.net.Authenticator;
import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth_service.excep.AlreadyExistException;
import com.example.auth_service.excep.GlobalExceptionHandler;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServ {
    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServ jwtServ;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthRes register(AuthReq req) {
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
                .birth(req.getBirth())
                .createdAt(Instant.now())
                .email(req.getEmail())
                .fullname(req.getFullname())
                .role(userRole)
                .build();

        String refresh = jwtServ.refreshToken(auth);
        String access = jwtServ.accessToken(auth);
        auth.setRefreshToken(refresh);
        return AuthRes.builder().access(access).refresh(refresh).build();

    }

    public AuthRes login(AuthReq req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        } catch (AuthenticationException ex) {
            throw new NotFound("Username not found");
        }
        Specification<Auth> spec = Specification
                .where(AuthSpeci.hasUsername(req.getUsername()));

        Auth auth = authRepo.findOne(spec).get();
        // Tạo token
        String access = jwtServ.accessToken(auth);
        String refresh = jwtServ.refreshToken(auth);
        auth.setRefreshToken(refresh);
        authRepo.save(auth);
        return AuthRes.builder().access(access).refresh(refresh).build();

    }

    public String refreshToken(String token) {
        String username = jwtServ.extractUsername(token);
        UserDetails auth = userDetailsService.loadUserByUsername(username);
        if (jwtServ.isRefreshToken(token) == false) {
            throw new UnauthorizedException("Invalid token or user not authorized");
        }

        String newAccessToken = jwtServ.accessToken(auth);
        return newAccessToken;

    }
}
