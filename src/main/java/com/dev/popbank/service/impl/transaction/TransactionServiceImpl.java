package com.dev.popbank.service.impl.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.dev.popbank.mapper.TransactionMapper;
import com.dev.popbank.model.dto.transaction.TransactionRequest;
import com.dev.popbank.model.dto.transaction.TransactionResponse;
import com.dev.popbank.model.transaction.TransactionEntity;
import com.dev.popbank.repository.TransactionRepository;
import com.dev.popbank.repository.UserRepository;
import com.dev.popbank.service.TransactionService;
import com.dev.popbank.service.WalletService;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, WalletService walletService, TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.walletService = walletService;
    }

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        var sender = userRepository.findById(transactionRequest.senderId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var receiver = userRepository.findById(transactionRequest.receiverId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Não é possível transferir para si mesmo");
        }

        BigDecimal balance = walletService.getBalance(sender.getId());
        
        if (balance.compareTo(transactionRequest.amount()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        walletService.addBalance(sender.getId(), transactionRequest.amount().negate());
        walletService.addBalance(receiver.getId(), transactionRequest.amount());

        TransactionEntity transactionEntity = transactionMapper.toTransactionEntity(transactionRequest);
        transactionEntity.setSender(sender);
        transactionEntity.setReceiver(receiver);

        transactionRepository.save(transactionEntity);
        return transactionMapper.toTransactionResponse(transactionEntity);
    }
        
    @Override
    @Transactional
    public boolean validateTransaction(TransactionRequest transactionRequest) {
        var sender = userRepository.findById(transactionRequest.senderId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var receiver = userRepository.findById(transactionRequest.receiverId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (sender.getId().equals(receiver.getId())) {
            return false;
        }

        BigDecimal balance = walletService.getBalance(sender.getId());
        
        if (balance.compareTo(transactionRequest.amount()) < 0) {
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public void processTransaction(TransactionRequest transactionRequest) {
        if (!validateTransaction(transactionRequest)) {
            throw new RuntimeException("Transação inválida");
        }
        createTransaction(transactionRequest);
    }

    @Override
    @Transactional
    public TransactionResponse getTransactionById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da transação não pode ser nulo");
        }

        var transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
        return transactionMapper.toTransactionResponse(transactionEntity);
    }

    @Override
    @Transactional
    public Page<TransactionResponse> getAllTransactions(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<TransactionEntity> transactionEntities = transactionRepository.findAll(pageable);
        return transactionMapper.toTransactionPage(transactionEntities);
    }

    @Override
    @Transactional
    public void deleteTransaction(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da transação não pode ser nulo");
        }

        var transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
        transactionRepository.delete(transactionEntity);
    }

    @Override
    @Transactional
    public void undoTransaction(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da transação não pode ser nulo");
        }

        var transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        walletService.addBalance(transactionEntity.getSender().getId(), transactionEntity.getAmount());
        walletService.withdrawBalance(transactionEntity.getReceiver().getId(), transactionEntity.getAmount());
        transactionRepository.delete(transactionEntity);
    }

}
