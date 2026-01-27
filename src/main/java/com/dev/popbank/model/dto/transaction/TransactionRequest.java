package com.dev.popbank.model.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

import com.dev.popbank.model.transaction.TransactionType;

public record TransactionRequest(
        @NotNull
        UUID senderId,
        @NotNull
        UUID receiverId,
        @NotNull
        @DecimalMin(value = "0.01", message = "O valor da transação deve ser maior que zero")
        BigDecimal amount,
        @NotNull
        TransactionType transactionType
) {
}
