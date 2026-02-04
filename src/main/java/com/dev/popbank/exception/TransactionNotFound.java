package com.dev.popbank.exception;

public class TransactionNotFound extends RuntimeException {
    public TransactionNotFound(String message) {
        super(message);
    }
}
