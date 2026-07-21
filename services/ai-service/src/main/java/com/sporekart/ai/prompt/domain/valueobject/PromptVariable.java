package com.sporekart.ai.prompt.domain.valueobject;

import java.util.Objects;

public record PromptVariable(
    String name,
    VariableType type,
    boolean required,
    String defaultValue,
    VariableValidation validation,
    String description,
    String example,
    boolean nullable,
    PromptVisibility visibility,
    boolean sensitive
) {
    public PromptVariable {
        Objects.requireNonNull(name, "Variable name must not be null");
        if (name.isBlank()) throw new IllegalArgumentException("Variable name must not be blank");
        Objects.requireNonNull(type, "Variable type must not be null");
        Objects.requireNonNull(visibility, "Visibility must not be null");
        validation = validation == null ? VariableValidation.none() : validation;
    }

    public boolean isValidValue(Object value) {
        if (value == null) return nullable || !required;
        return switch (type) {
            case STRING -> value instanceof String;
            case INTEGER -> value instanceof Integer || value instanceof Long;
            case BOOLEAN -> value instanceof Boolean;
            case DECIMAL -> value instanceof Double || value instanceof Float;
            case JSON -> value instanceof String;
            case LIST -> value instanceof java.util.List;
            case OBJECT -> value instanceof java.util.Map;
            case ENUM -> value instanceof String;
            case DATE -> value instanceof String;
        };
    }

    public boolean hasDefault() {
        return defaultValue != null && !defaultValue.isEmpty();
    }
}
