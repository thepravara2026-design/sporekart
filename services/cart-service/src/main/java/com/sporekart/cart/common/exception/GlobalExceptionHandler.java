package com.sporekart.cart.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleCartNotFoundException(CartNotFoundException ex) {
        LOGGER.warn("Cart not found", ex);
        return buildResponse(HttpStatus.NOT_FOUND, "cart.not.found", ex.getMessage(), null);
    }

    @ExceptionHandler(InvalidCartOperationException.class)
    public ResponseEntity<ProblemDetails> handleInvalidCartOperationException(InvalidCartOperationException ex) {
        LOGGER.warn("Invalid cart operation", ex);
        return buildResponse(HttpStatus.BAD_REQUEST, "cart.invalid.operation", ex.getMessage(), null);
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ProblemDetails> handleCartException(CartException ex) {
        LOGGER.error("Cart exception", ex);
        return buildResponse(HttpStatus.BAD_REQUEST, "cart.error", ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleValidationException(MethodArgumentNotValidException ex) {
        LOGGER.warn("Validation failure", ex);
        return buildResponse(HttpStatus.BAD_REQUEST, "validation.error", "Request validation failed", null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetails> handleConstraintViolation(ConstraintViolationException ex) {
        LOGGER.warn("Constraint violation", ex);
        return buildResponse(HttpStatus.BAD_REQUEST, "validation.error", "Constraint validation failed", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGenericException(Exception ex) {
        LOGGER.error("Unhandled exception", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "server.error", "Unexpected server error", null);
    }

    private ResponseEntity<ProblemDetails> buildResponse(HttpStatus status, String type, String detail,
            String instance) {
        Map<String, Object> extensions = new LinkedHashMap<>();
        extensions.put("timestamp", OffsetDateTime.now());
        ProblemDetails problem = new ProblemDetails(
                "/problems/" + type,
                status.getReasonPhrase(),
                status.value(),
                detail,
                instance,
                OffsetDateTime.now(),
                extensions);
        return ResponseEntity.status(status).body(problem);
    }
}