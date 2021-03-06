package com.bortoti.accountmanagement.exception;

public class TransactionLimitExceededException extends RuntimeException {
    public TransactionLimitExceededException() {
        super("Transaction Limit Exceeded");
    }
}