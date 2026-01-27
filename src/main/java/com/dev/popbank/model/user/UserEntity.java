package com.dev.popbank.model.user;

import com.dev.popbank.model.auth.ContaEntity;
import com.dev.popbank.model.wallet.WalletEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private String nomeDaMae;
    @Column(unique = true)
    private String cpf;
    private Date dataNascimento;
    private String senha;
    @OneToOne
    @JoinColumn(name = "wallet_id")
    private WalletEntity wallet;
    @OneToOne(mappedBy = "usuario")
    private ContaEntity conta;
    private boolean ativo;
}
