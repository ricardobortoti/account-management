package com.bortoti.accountmanagement.exception;

import java.io.Serializable;
import java.util.function.Supplier;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public static <T extends Serializable> Supplier<AccountNotFoundException> notFound(final T entityId) {
        return () -> new AccountNotFoundException("Could not find account number " + entityId);
    }
}