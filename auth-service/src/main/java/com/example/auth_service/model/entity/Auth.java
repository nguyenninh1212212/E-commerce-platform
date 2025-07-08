package com.example.auth_service.model.entity;

import java.sql.Date;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auth implements UserDetails {
        @Id
        @UuidGenerator
        private UUID id;
        private String username;
        private String password;
        private String googleId;
        private String email;
        private String phoneNumber;
        @Column(name = "refresh_token", columnDefinition = "TEXT")
        private String refreshToken;
        private Instant createdAt;
        private Instant updatedAt;
        private Instant deletedAt;
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "auth_role", joinColumns = @JoinColumn(name = "auth_id"), inverseJoinColumns = @JoinColumn(name = "role_name"))
        private List<Role> role;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return role.stream()
                                .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                                .toList();
        }

}
