package com.dev.popbank.model.wallet;

import com.dev.popbank.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
    private UserEntity user;
    private String nome;
    private BigDecimal balance;
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavingsEntity> savingsAccounts;

    public void addSavingsAccount(WalletEntity wallet) {
        SavingsEntity savingsEntity = SavingsEntity.builder()
                .wallet(wallet)
                .balance(new BigDecimal("0.00"))
                .build();
        savingsAccounts.add(savingsEntity);
    }

    public void removeSavingsAccount(int index, WalletEntity wallet) {
        if (index < 0 || index >= savingsAccounts.size()) {
            throw new IndexOutOfBoundsException("Índice de conta poupança inválido");
        }

        var devolution = savingsAccounts.get(index).getBalance();
        wallet.balance = wallet.balance.add(devolution);
        savingsAccounts.remove(index);
    }
}
