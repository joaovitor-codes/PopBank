package com.dev.popbank.service;

import com.dev.popbank.model.wallet.SavingsEntity;
import com.dev.popbank.model.wallet.WalletEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface WalletService {
    WalletEntity createWallet(UUID userId);
    void deleteWallet(UUID userId);
    BigDecimal getBalance(UUID userId);
    void addBalance(UUID userId, java.math.BigDecimal amount);
    List<SavingsEntity> getSavingsAccounts(UUID userId);
    void createSaving(UUID userId);
    void deleteSaving(UUID userId, int index);
    void switchSaving(UUID userId, int fromIndex, int toIndex, BigDecimal amount);
    void withdrawBalance(UUID userId, BigDecimal amount);
}
