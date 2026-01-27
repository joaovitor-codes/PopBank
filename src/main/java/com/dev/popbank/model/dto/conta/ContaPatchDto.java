package com.dev.popbank.model.dto.conta;

import jakarta.validation.constraints.Email;

import java.util.Optional;

public record ContaPatchDto(
        @Email(message = "Email inválido")
        Optional<String> login,
        Optional<String> senha
) {
}
