package com.bortoti.accountmanagement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.NestedServletException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> constraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException");
        List<AttributeMessage> attributeMessages = new ArrayList<>();

        String field = "";
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            for (Path.Node node : violation.getPropertyPath()) {
                field = node.getName();
            }
            attributeMessages.add(new AttributeMessage(field, violation.getMessage()));
        }

        ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage(), attributeMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(NestedServletException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> nestedServletException(NestedServletException ex) {
        log.error("NestedServletException");
        ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException");
        ExceptionResponse err = null;
        if (e.getBindingResult().getGlobalError() != null) {
            err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getBindingResult().getGlobalError().getDefaultMessage());
        } else {
            err = new ExceptionResponse(HttpStatus.BAD_REQUEST);
        }

        for (FieldError x : e.getBindingResult().getFieldErrors()) {
            err.getAttributes().add(new AttributeMessage(x.getField(), x.getDefaultMessage()));
        }
        err.setDescription("Validation Errors");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handlingException(Exception ex) {
        log.error("Exception");
        ExceptionResponse err = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> dataIntegrityViolationException(DataIntegrityViolationException e) {
        ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, "Database Error");
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AccountNumberAlreadyExistsException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> dataIntegrityViolationException(AccountNumberAlreadyExistsException e) {
        ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> dataIntegrityViolationException(AccountNotFoundException e) {
        ExceptionResponse err = new ExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage());
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> notEnoughBalanceException(NotEnoughBalanceException e) {
        ExceptionResponse err = new ExceptionResponse(HttpStatus.CONFLICT, e.getMessage());
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(TransactionLimitExceededException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> transactionLimitExceededException(TransactionLimitExceededException e) {
        ExceptionResponse err = new ExceptionResponse(HttpStatus.CONFLICT, e.getMessage());
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(SameAccountException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> sameAccountException(SameAccountException e) {
        ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
