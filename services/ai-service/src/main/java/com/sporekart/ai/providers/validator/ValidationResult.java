package com.sporekart.ai.providers.validator;

import java.util.List;

public record ValidationResult(
    boolean passed,
    List<String> errors,
    List<String> warnings,
    List<String> recommendations
) {
    public static ValidationResult valid() {
        return new ValidationResult(true, List.of(), List.of(), List.of());
    }

    public static ValidationResult invalid(List<String> errors) {
        return new ValidationResult(false, errors, List.of(), List.of());
    }

    public boolean isValid() { return passed; }
}
