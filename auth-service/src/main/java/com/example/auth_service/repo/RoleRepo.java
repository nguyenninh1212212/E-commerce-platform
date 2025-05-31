package com.example.auth_service.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.auth_service.model.entity.Role;
import com.example.auth_service.model.enums.ROLE;

public interface RoleRepo extends JpaRepository<Role, ROLE> {
    @Query("SELECT s FROM Role s WHERE s.name =:name ")
    Optional<Role> findByName(ROLE name);
}