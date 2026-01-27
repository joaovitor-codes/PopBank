package com.dev.popbank.model.dto.conta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContaRequest(
        @Email(message = "Email inválido")
        String login,
        @NotBlank(message = "A password é obrigatória")
        String password
) {
}
