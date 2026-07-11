package com.sporekart.ai.core.application.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ValidationResult {
    private final List<String> errors;
    private final List<String> warnings;

    private ValidationResult(List<String> errors, List<String> warnings) {
        this.errors = Collections.unmodifiableList(errors);
        this.warnings = Collections.unmodifiableList(warnings);
    }

    public static ValidationResult valid() {
        return new ValidationResult(List.of(), List.of());
    }

    public static ValidationResult withError(String error) {
        return new ValidationResult(List.of(error), List.of());
    }

    public boolean isValid() { return errors.isEmpty(); }
    public boolean hasWarnings() { return !warnings.isEmpty(); }
    public List<String> getErrors() { return errors; }
    public List<String> getWarnings() { return warnings; }

    public static class Builder {
        private final List<String> errors = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();

        public Builder addError(String error) { errors.add(error); return this; }
        public Builder addWarning(String warning) { warnings.add(warning); return this; }
        public ValidationResult build() { return new ValidationResult(new ArrayList<>(errors), new ArrayList<>(warnings)); }
    }
}
