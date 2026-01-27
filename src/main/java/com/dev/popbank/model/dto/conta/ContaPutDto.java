package com.dev.popbank.model.dto.conta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContaPutDto(
        @Email(message = "Email invalido")
        @NotBlank
        String login,
        @NotBlank
        String senha
) {
}
