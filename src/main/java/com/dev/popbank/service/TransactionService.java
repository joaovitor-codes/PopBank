package com.dev.popbank.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.dev.popbank.model.dto.transaction.TransactionRequest;
import com.dev.popbank.model.dto.transaction.TransactionResponse;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    boolean validateTransaction(TransactionRequest transactionRequest);
    void processTransaction(TransactionRequest transactionRequest);
    TransactionResponse getTransactionById(UUID id);
    Page<TransactionResponse> getAllTransactions(int pageNumber);
    void deleteTransaction(UUID id);
    void undoTransaction(UUID id);
}
