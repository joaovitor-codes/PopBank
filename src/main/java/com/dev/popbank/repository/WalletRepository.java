package com.dev.popbank.repository;

import com.dev.popbank.model.wallet.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    WalletEntity findByUserId(UUID userId);
}
