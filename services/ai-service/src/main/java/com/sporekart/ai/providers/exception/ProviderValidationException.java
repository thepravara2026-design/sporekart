package com.sporekart.ai.providers.exception;

import java.util.List;

public class ProviderValidationException extends ProviderException {
    private final List<String> validationErrors;

    public ProviderValidationException(String providerId, List<String> errors) {
        super("PROVIDER_VALIDATION_ERROR", "Validation failed for provider " + providerId + ": " + String.join(", ", errors), 400);
        this.validationErrors = errors;
    }

    public List<String> getValidationErrors() { return validationErrors; }
}
