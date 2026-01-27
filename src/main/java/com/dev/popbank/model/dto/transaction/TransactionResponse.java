package com.dev.popbank.model.dto.transaction;

import com.dev.popbank.model.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID senderId,
        UUID receiverId,
        BigDecimal amount,
        LocalDateTime transactionDate,
        TransactionType transactionType
) {
}
