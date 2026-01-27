package com.dev.popbank.model.dto.conta;

import com.dev.popbank.model.auth.ContasRole;

import java.util.UUID;

public record ContaResponse(
        UUID id,
        UUID idUsuario,
        String login,
        ContasRole role
) {
}
