package com.example.auth_service.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.auth_service.model.entity.Auth;

@Repository
public interface AuthRepo extends JpaRepository<Auth, UUID>, JpaSpecificationExecutor<Auth> {

    Optional<Auth> findByUsername(String username);

}
