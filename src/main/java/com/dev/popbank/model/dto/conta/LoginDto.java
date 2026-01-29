package com.dev.popbank.model.dto.conta;

import java.util.UUID;

public record LoginDto(String token, UUID userId) {
}
