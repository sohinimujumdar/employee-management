package com.example.employee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Bad Request: Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = "Validation failed: " + String.join("; ", errors);
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // 404 - Not Found: Custom EmployeeNotFoundException
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // 404 - Not Found: ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // 403 - Forbidden: Access denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDenied(AccessDeniedException ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.FORBIDDEN.value(), "Access denied: " + ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    // 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
