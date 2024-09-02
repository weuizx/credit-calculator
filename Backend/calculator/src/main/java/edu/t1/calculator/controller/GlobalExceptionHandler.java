package edu.t1.calculator.controller;

import edu.t1.calculator.controller.dto.ErrorResponse;
import edu.t1.calculator.controller.exceptions.CreditNotFoundException;
import edu.t1.calculator.controller.exceptions.GenerateXlsxException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CreditNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCreditNotFoundException(CreditNotFoundException e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(404)).body(body);
    }

    @ExceptionHandler(GenerateXlsxException.class)
    public ResponseEntity<ErrorResponse> handleGenerateXlsxException(GenerateXlsxException e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(500)).body(body);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(500)).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder("Validation errors: ");
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return ResponseEntity.badRequest().body(new ErrorResponse(errors.toString()));
    }
}
