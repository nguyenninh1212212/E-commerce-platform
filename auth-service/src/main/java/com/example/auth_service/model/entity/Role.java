package com.example.auth_service.model.entity;

import com.example.auth_service.model.enums.ROLE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ROLE name;

    public void orElseGet(Object object) {
        throw new UnsupportedOperationException("Unimplemented method 'orElseGet'");
    }
}
