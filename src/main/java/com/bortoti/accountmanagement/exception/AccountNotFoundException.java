package com.bortoti.accountmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;
import java.util.function.Supplier;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not find account number")
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public static <T extends Serializable> Supplier<AccountNotFoundException> notFound(final T entityId) {
        return () -> new AccountNotFoundException("Could not find account number " + entityId);
    }
}