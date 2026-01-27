package com.dev.popbank.repository;

import com.dev.popbank.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByCpf(String cpf);
}
