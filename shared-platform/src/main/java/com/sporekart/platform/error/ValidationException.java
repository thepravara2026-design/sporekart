package com.sporekart.platform.error;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ValidationException extends SporekartException {
    private final List<ValidationError> validationErrors;

    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message, message, 400);
        this.validationErrors = List.of();
    }

    public ValidationException(String message, List<ValidationError> errors) {
        super(ErrorCode.VALIDATION_ERROR, message, message, 400);
        this.validationErrors = errors != null ? List.copyOf(errors) : List.of();
    }

    public ValidationException(String field, String message) {
        super(ErrorCode.VALIDATION_ERROR, message, message, 400);
        this.validationErrors = List.of(new ValidationError(field, message));
    }

    public List<ValidationError> getValidationErrors() { return validationErrors; }

    public record ValidationError(String field, String message, String code) {
        public ValidationError(String field, String message) {
            this(field, message, "INVALID_INPUT");
        }
    }
}
