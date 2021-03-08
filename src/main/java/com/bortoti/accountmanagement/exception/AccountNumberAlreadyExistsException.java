package com.bortoti.accountmanagement.exception;

public class AccountNumberAlreadyExistsException extends RuntimeException {
    public AccountNumberAlreadyExistsException() {
        super("Account number already exists");
    }
}