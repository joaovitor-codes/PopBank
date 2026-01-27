package com.dev.popbank.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record UserRequest(
        @NotBlank
        String nome,
        @NotBlank
        String nomeDaMae,
        @CPF
        String cpf,
        @Past
        Date dataNascimento,
        String senha
) {
}
