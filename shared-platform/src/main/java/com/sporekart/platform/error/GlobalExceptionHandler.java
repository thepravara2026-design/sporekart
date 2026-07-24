package com.sporekart.platform.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SporekartException.class)
    public ResponseEntity<ProblemDetails> handleSporekartException(SporekartException ex, WebRequest request) {
        ProblemDetails body = ProblemDetails.fromException(ex, request.getDescription(false), getTraceId());
        log.warn("Business exception: {} (status={})", ex.getMessage(), ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        List<ValidationException.ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ValidationException.ValidationError(e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());

        ProblemDetails body = ProblemDetails.builder()
                .type("https://api.sporekart.com/errors/validation_error")
                .title("Validation failed")
                .status(400)
                .detail("One or more fields failed validation")
                .instance(request.getDescription(false))
                .traceId(getTraceId())
                .errorCode("VALIDATION_ERROR")
                .extensions(java.util.Map.of("validationErrors", errors))
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetails> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        ProblemDetails body = ProblemDetails.builder()
                .type("https://api.sporekart.com/errors/forbidden")
                .title("Access denied")
                .status(403)
                .detail(ex.getMessage())
                .instance(request.getDescription(false))
                .traceId(getTraceId())
                .errorCode("FORBIDDEN")
                .build();

        return ResponseEntity.status(403).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetails> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        ProblemDetails body = ProblemDetails.builder()
                .type("https://api.sporekart.com/errors/bad_request")
                .title("Bad request")
                .status(400)
                .detail(ex.getMessage())
                .instance(request.getDescription(false))
                .traceId(getTraceId())
                .errorCode("BAD_REQUEST")
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGeneral(Exception ex, WebRequest request) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        ProblemDetails body = ProblemDetails.builder()
                .type("https://api.sporekart.com/errors/internal_error")
                .title("Internal server error")
                .status(500)
                .detail("An unexpected error occurred. Please try again later.")
                .instance(request.getDescription(false))
                .traceId(getTraceId())
                .errorCode("INTERNAL_ERROR")
                .build();

        return ResponseEntity.status(500).body(body);
    }

    private String getTraceId() {
        String traceId = MDC.get("traceId");
        return traceId != null ? traceId : "unknown";
    }
}
