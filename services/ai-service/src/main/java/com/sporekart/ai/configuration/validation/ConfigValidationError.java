package com.sporekart.ai.configuration.validation;

public record ConfigValidationError(
    String key,
    String message,
    String expectedValue,
    String actualValue,
    ValidationSeverity severity,
    String validatorName
) {
    public static ConfigValidationError error(String key, String message) {
        return new ConfigValidationError(key, message, "", "", ValidationSeverity.ERROR, "default");
    }

    public static ConfigValidationError warning(String key, String message) {
        return new ConfigValidationError(key, message, "", "", ValidationSeverity.WARNING, "default");
    }
}
