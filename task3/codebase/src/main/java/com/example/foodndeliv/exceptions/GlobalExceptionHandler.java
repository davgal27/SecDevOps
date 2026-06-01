package com.example.foodndeliv.exceptions;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.persistence.*;


import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Template code to extract individual validation errors – @Valid may produce multiple messages
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        // Collect constraint violation details
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Object> handleValidationExceptions(PersistenceException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", "Persistence error"); 

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    } 
    
    @ExceptionHandler(DomainInvariantException.class)
    public ResponseEntity<Object> handleValidationExceptions(DomainInvariantException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // Add specific messaging for any illegal HTTP requests (405s were being converted to 400s)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleValidationExceptions(HttpRequestMethodNotSupportedException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        response.put("errors", "Method not allowed"); 

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }  

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {

        ex.printStackTrace();

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", "Application error: " + ex.getClass().getName());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Filter out any detailed messages - tackle issues with tracing instead
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<Object> handleValidationExceptions(Exception ex) {

    //     Map<String, Object> response = new HashMap<>();
    //     response.put("timestamp", System.currentTimeMillis());
    //     response.put("status", HttpStatus.BAD_REQUEST.value());
    //     response.put("errors", "Application error"); 

    //     return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    // }     
}    

