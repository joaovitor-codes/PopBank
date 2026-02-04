package com.dev.popbank.model.user;

import com.dev.popbank.model.auth.ContaEntity;
import com.dev.popbank.model.wallet.WalletEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id")
    @ToString.Exclude
    private WalletEntity wallet;
    @OneToOne(mappedBy = "usuario")
    private ContaEntity conta;
    private boolean ativo;
}
