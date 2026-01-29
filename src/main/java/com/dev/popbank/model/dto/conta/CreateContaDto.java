package com.dev.popbank.model.dto.conta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record CreateContaDto(
        @NotBlank
        @Email(message = "Email inválido")
        String login,
        @NotBlank(message = "A password é obrigatória")
        String password,
        @NotBlank
        String nome,
        @NotBlank
        String nomeDaMae,
        @NotBlank
        @CPF
        String cpf,
        @Past
        Date dataNascimento,
        @NotBlank
        String senhaTransacao
) {
}
