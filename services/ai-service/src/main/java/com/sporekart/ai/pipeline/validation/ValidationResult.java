package com.sporekart.ai.pipeline.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record ValidationResult(boolean valid, List<String> errors, List<String> warnings) {

    public static final ValidationResult VALID = new ValidationResult(true, List.of(), List.of());

    public ValidationResult {
        errors = errors == null ? List.of() : List.copyOf(errors);
        warnings = warnings == null ? List.of() : List.copyOf(warnings);
    }

    public static ValidationResult invalid(String error) {
        return new ValidationResult(false, List.of(error), List.of());
    }

    public static ValidationResult invalid(List<String> errors) {
        return new ValidationResult(false, errors, List.of());
    }

    public ValidationResult withWarning(String warning) {
        var newWarnings = new ArrayList<>(this.warnings);
        newWarnings.add(warning);
        return new ValidationResult(this.valid, this.errors, newWarnings);
    }

    public String firstError() {
        return errors.isEmpty() ? null : errors.get(0);
    }
}
