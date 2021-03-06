package com.bortoti.accountmanagement.exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException() {
        super("Not Enough Balance for Operation");
    }
}
