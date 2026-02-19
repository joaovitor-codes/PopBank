package com.dev.popbank.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record UserPut(
        @NotBlank
        String nome,
        @NotBlank
        String nomeDaMae,
        @NotBlank
        @CPF
        String cpf,
        @NotBlank
        @Past
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date dataNascimento,
        @NotBlank
        String senha
) {
}
