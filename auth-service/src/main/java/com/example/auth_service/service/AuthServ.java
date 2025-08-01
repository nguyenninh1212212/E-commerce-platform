package com.example.auth_service.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.checkerframework.checker.units.qual.m;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.example.auth_service.excep.AlreadyExistException;
import com.example.auth_service.excep.NotFound;
import com.example.auth_service.excep.UnauthorizedException;
import com.example.auth_service.model.Mail;
import com.example.auth_service.model.dto.req.AuthReq;
import com.example.auth_service.model.dto.req.OtpReq;
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
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtServ jwtServ;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtDecoder jwtDecoder;
    private final KafkaProducer kafkaProducer;
    private final OtpService otpService;

    public void preRegister(String email) {
        Specification<Auth> spec2 = Specification.where(AuthSpeci.hasEmail(email));
        if (authRepo.findOne(spec2).isPresent()) {
            throw new AlreadyExistException("Email already exist");
        }
        OTP(email, "reg");
    }

    public String verifyEmail(String email, String otp) {
        otpService.verifyOtp("verify:" + email, otp);

        redisTemplate.opsForValue().set("email:verified:" + email, "true", Duration.ofMinutes(10));

        return "Xác thực email thành công";
    }

    public String register(AuthReq req) {
        Boolean verified = redisTemplate.hasKey("email:verified:" + req.getEmail());
        if (!Boolean.TRUE.equals(verified)) {
            throw new RuntimeException("Email chưa xác thực");
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
        redisTemplate.delete("email:verified:" + req.getEmail());
        return "Register successfully!!";

    }

    public void login(AuthReq req) {
        try {
            String username = actualUsername(req.getUsername()).getUsername();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,
                            req.getPassword()));
            Auth auth = (Auth) authentication.getPrincipal();
            preLogin(auth.getEmail());
        } catch (AuthenticationException ex) {
            throw new NotFound("Username not found");
        }

    }

    public AuthRes verifyOtpAndLogin(OtpReq req) {
        Auth auth = authRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new NotFound("User"));

        String cacheKey = "log:" + auth.getEmail();
        String cachedOtp = redisTemplate.opsForValue().get(cacheKey);
        if (cachedOtp == null || !cachedOtp.equals(req.getOtp())) {
            throw new UnauthorizedException("OTP invalid or expired");
        }
        redisTemplate.delete(cacheKey);
        String access = jwtServ.accessToken(auth);
        String refresh = jwtServ.refreshToken(auth);
        auth.setRefreshToken(refresh);
        authRepo.save(auth);
        return AuthRes.builder()
                .access(access)
                .refresh(refresh)
                .build();
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

    private Auth actualUsername(String username) {
        Optional<Auth> userOpt = authRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            userOpt = authRepo.findByEmail(username);
        }
        if (userOpt.isEmpty()) {
            throw new NotFound("User not found");
        }
        return userOpt.get();
    }

    private void checkExist(AuthReq req) {
        Specification<Auth> spec = Specification.where(AuthSpeci.hasUsername(req.getUsername()));
        Specification<Auth> spec2 = Specification.where(AuthSpeci.hasEmail(req.getEmail()));

        if (authRepo.findOne(spec).isPresent()) {
            throw new AlreadyExistException("User already exist");
        }
        if (authRepo.findOne(spec2).isPresent()) {
            throw new AlreadyExistException("Email already exist");
        }
    }

    private void preLogin(String username) {
        String email = actualUsername(username).getEmail();
        OTP(email, "log:");
    }

    private void OTP(String email, String key) {
        String otp = otpService.generateAndStoreOtp(email);
        Mail mail = new Mail();
        mail.setContent(otp);
        mail.setEmail(email);
        mail.setSubject("Confirm OTP");
        kafkaProducer.sendEventOtp(mail);
        redisTemplate.opsForValue().set(key + email, otp, Duration.ofMillis(1));
    }

}
