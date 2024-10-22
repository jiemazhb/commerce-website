package com.microservice.identity_service.repository;

import com.microservice.identity_service.dto.UserResponse;
import com.microservice.identity_service.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {
    Optional<UserCredential> findByName(String username);

    Optional<UserCredential> findByEmail(String email);
}
