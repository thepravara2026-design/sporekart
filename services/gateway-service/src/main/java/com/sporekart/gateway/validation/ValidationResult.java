package com.sporekart.gateway.validation;

import java.util.ArrayList;
import java.util.List;

public record ValidationResult(
    boolean valid,
    List<String> errors,
    List<String> warnings
) {
    public static ValidationResult ok() {
        return new ValidationResult(true, List.of(), List.of());
    }

    public static ValidationResult failed(String error) {
        return new ValidationResult(false, List.of(error), List.of());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<String> errors = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();

        public Builder error(String msg) { errors.add(msg); return this; }
        public Builder warn(String msg) { warnings.add(msg); return this; }
        public boolean hasErrors() { return !errors.isEmpty(); }

        public ValidationResult build() {
            return new ValidationResult(errors.isEmpty(), List.copyOf(errors), List.copyOf(warnings));
        }
    }
}
