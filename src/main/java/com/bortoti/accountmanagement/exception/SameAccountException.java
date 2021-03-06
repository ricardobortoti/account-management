package com.bortoti.accountmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Origin and destiny account are the same")
public class SameAccountException extends RuntimeException {
    public SameAccountException() {
        super("Origin and destiny account are the same");
    }
}