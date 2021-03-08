package com.bortoti.accountmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SameAccountException extends RuntimeException {
    public SameAccountException() {
        super("Origin and destiny account are the same");
    }
}