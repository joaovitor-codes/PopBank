package com.dev.popbank.mapper;

import org.springframework.data.domain.Page;

import com.dev.popbank.model.dto.transaction.TransactionRequest;
import com.dev.popbank.model.dto.transaction.TransactionResponse;
import com.dev.popbank.model.transaction.TransactionEntity;

public interface TransactionMapper {
    TransactionResponse toTransactionResponse(TransactionEntity transactionEntity);
    TransactionEntity toTransactionEntity(TransactionRequest transactionRequest);
    TransactionResponse toTransactionGet(TransactionEntity transactionEntity);
    Page<TransactionResponse> toTransactionPage(Page<TransactionEntity> transactionEntities);
}
