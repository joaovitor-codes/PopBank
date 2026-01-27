package com.dev.popbank.model.dto.user;

import java.util.Date;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String nome,
        String nomeDaMae,
        String cpf,
        Date dataNascimento,
        boolean ativo
) {
}
