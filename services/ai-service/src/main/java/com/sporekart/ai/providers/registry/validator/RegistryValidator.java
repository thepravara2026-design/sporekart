package com.sporekart.ai.providers.registry.validator;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

public interface RegistryValidator {
    ValidationResult validate(ProviderCatalogEntry entry);
    boolean supports(String validationType);
}

record ValidationResult(boolean valid, String errorMessage) {
    static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
}
