package com.dev.popbank.service.impl.Wallet;

import com.dev.popbank.model.wallet.SavingsEntity;
import com.dev.popbank.model.wallet.WalletEntity;
import com.dev.popbank.repository.UserRepository;
import com.dev.popbank.repository.WalletRepository;
import com.dev.popbank.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public WalletServiceImpl(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public WalletEntity createWallet(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        WalletEntity walletEntity = WalletEntity.builder()
                .user(user)
                .balance(new BigDecimal("0.00"))
                .build();
        walletRepository.save(walletEntity);
        return walletEntity;
    }

    @Override
    @Transactional
    public void deleteWallet(UUID userId) {
        WalletEntity walletEntity = walletRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
        walletRepository.delete(walletEntity);
    }

    @Override
    @Transactional
    public BigDecimal getBalance(UUID userId) {
        WalletEntity walletEntity = walletRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
        return walletEntity.getBalance();
    }

    @Override
    @Transactional
    public void addBalance(UUID userId, BigDecimal amount) {
        WalletEntity walletEntity = walletRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
        walletEntity.setBalance(getBalance(userId).add(amount));
        walletRepository.save(walletEntity);
    }

    @Override
    @Transactional
    public void withdrawBalance(UUID userId, BigDecimal amount) {
        WalletEntity walletEntity = walletRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
        walletEntity.setBalance(getBalance(userId).subtract(amount));
        walletRepository.save(walletEntity);
    }

    @Override
    @Transactional
    public List<SavingsEntity> getSavingsAccounts(UUID userId) {
        WalletEntity walletEntity = walletRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
        return walletEntity.getSavingsAccounts();
    }

    @Override
    @Transactional
    public void createSaving(UUID userId) {
        WalletEntity wallet = walletRepository.findByUserId(userId);

        if (wallet == null) {
            throw new RuntimeException("Carteira não encontrada");
        }

        wallet.addSavingsAccount(wallet);
    }

    @Override
    @Transactional
    public void deleteSaving(UUID userId, int index) {
        WalletEntity wallet = walletRepository.findByUserId(userId);

        if (index < 0 || index >= wallet.getSavingsAccounts().size()) {
            throw new IndexOutOfBoundsException("Índice de conta poupança inválido");
        }

        if (wallet == null || wallet.getSavingsAccounts().isEmpty()) {
            throw new RuntimeException("Carteira não encontrada");
        }

        wallet.removeSavingsAccount(index, wallet);
    }

    @Override
    @Transactional
    public void switchSaving(UUID userId, int fromIndex, int toIndex, BigDecimal amount) {
        WalletEntity wallet = walletRepository.findByUserId(userId);

        if (wallet == null) {
            throw new RuntimeException("Carteira não encontrada");
        }

        List<SavingsEntity> savingsAccounts = wallet.getSavingsAccounts();

        if (fromIndex < 0 || fromIndex >= savingsAccounts.size() ||
                toIndex < 0 || toIndex >= savingsAccounts.size()) {
            throw new IndexOutOfBoundsException("Índice de conta poupança inválido");
        }

        SavingsEntity fromAccount = savingsAccounts.get(fromIndex);
        SavingsEntity toAccount = savingsAccounts.get(toIndex);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Saldo insuficiente na conta poupança de origem");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
    }
}
