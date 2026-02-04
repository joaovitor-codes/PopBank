package com.dev.popbank.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String nome,
        String nomeDaMae,
        String cpf,
        @JsonFormat(pattern = "dd/MM/yyyy")
        Date dataNascimento,
        boolean ativo
) {
}
