package com.dev.popbank.model.dto.conta;

import com.dev.popbank.model.auth.ContasRole;

public record RegisterAdmDto(
        String login,
        String senha,
        ContasRole role
) {
}
