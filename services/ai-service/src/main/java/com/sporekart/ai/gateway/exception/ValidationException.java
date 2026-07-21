package com.sporekart.ai.gateway.exception;

import java.util.List;

public class ValidationException extends GatewayException {
    private final List<String> validationErrors;

    public ValidationException(List<String> validationErrors) {
        super("VALIDATION_ERROR", "Request validation failed: " + String.join(", ", validationErrors), 400);
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() { return validationErrors; }
}
