package com.example.auth_service.config;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.auth_service.model.entity.Auth;
import com.example.auth_service.model.entity.Role;
import com.example.auth_service.model.enums.ROLE;
import com.example.auth_service.repo.AuthRepo;
import com.example.auth_service.repo.AuthSpeci;
import com.example.auth_service.repo.RoleRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitialize implements CommandLineRunner {
    private final RoleRepo roleRepo;
    private final PasswordEncoder encoder;
    private final AuthRepo authRepo;

    @Override
    public void run(String... args) throws Exception {
        Stream.of(ROLE.ADMIN, ROLE.USER, ROLE.MANAGER, ROLE.MARKETING, ROLE.ARTIST, ROLE.REPORT,
                ROLE.SUPPORT, ROLE.SELLER)
                .forEach(this::createRole);

        String username = "admin";
        Specification<Auth> spec = Specification.where(AuthSpeci.hasUsername(username));

        authRepo.findOne(spec).orElseGet(() -> {
            String password = encoder.encode("1234");
            Auth auth = Auth.builder()
                    .createdAt(Instant.now())
                    .email("abc123@gmail.com")
                    .password(password)
                    .username(username)
                    .role(List.of(roleRepo.getById(ROLE.ADMIN)))
                    .build();
            return authRepo.save(auth);
        });
    }

    private void createRole(ROLE rolename) {
        roleRepo.findByName(rolename).orElseGet(() -> {
            Role role = new Role();
            role.setName(rolename);
            return roleRepo.save(role);
        });
    }
}
