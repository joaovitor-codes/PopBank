package com.dev.popbank.repository;

import com.dev.popbank.model.auth.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface ContaRepository extends JpaRepository<ContaEntity, UUID> {
    UserDetails findByLogin(String login);
    boolean existsByLogin(String login);
}
