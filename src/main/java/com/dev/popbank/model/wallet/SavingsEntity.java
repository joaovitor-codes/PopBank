package com.dev.popbank.model.wallet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_savings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private WalletEntity wallet;
    private BigDecimal balance;

}
