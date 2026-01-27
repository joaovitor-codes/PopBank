package com.dev.popbank.mapper.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.dev.popbank.mapper.TransactionMapper;
import com.dev.popbank.model.dto.transaction.TransactionRequest;
import com.dev.popbank.model.dto.transaction.TransactionResponse;
import com.dev.popbank.model.transaction.TransactionEntity;

@Component
public class TransactionMapperImpl implements TransactionMapper {
    
    @Override
    public TransactionResponse toTransactionResponse(TransactionEntity transactionEntity) {
        return new TransactionResponse(transactionEntity.getId(),
            transactionEntity.getSender().getId(),
            transactionEntity.getReceiver().getId(),
            transactionEntity.getAmount(),
            transactionEntity.getTransactionDate(),
            transactionEntity.getTransactionType());
    }

     @Override
    public TransactionEntity toTransactionEntity(TransactionRequest transactionRequest) {
        return TransactionEntity.builder()
            .amount(transactionRequest.amount())
            .transactionDate(LocalDateTime.now())
            .transactionType(transactionRequest.transactionType())
            .build();
    }

     @Override
    public TransactionResponse toTransactionGet(TransactionEntity transactionEntity) {
        return new TransactionResponse(transactionEntity.getId(),
            transactionEntity.getSender().getId(),
            transactionEntity.getReceiver().getId(),
            transactionEntity.getAmount(),
            transactionEntity.getTransactionDate(),
            transactionEntity.getTransactionType());
    }

    @Override
    public Page<TransactionResponse> toTransactionPage(Page<TransactionEntity> transactionEntities) {
        if (transactionEntities.isEmpty()) {
            throw new NullPointerException("A lista de transações está vazia");
        }

        return transactionEntities.map(this::toTransactionResponse);
    }
}
