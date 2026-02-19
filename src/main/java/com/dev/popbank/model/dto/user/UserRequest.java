package com.dev.popbank.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record UserRequest(
        @NotBlank
        String nome,
        @NotBlank
        String nomeDaMae,
        @CPF
        String cpf,
        @Past
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date dataNascimento,
        String senha
) {
}
