package com.sporekart.ai.prompt.domain.valueobject;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public record VariableValidation(
    Pattern regex,
    List<Object> allowedValues,
    Integer minLength,
    Integer maxLength,
    Double minValue,
    Double maxValue
) {
    public VariableValidation {
        allowedValues = allowedValues == null ? List.of() : List.copyOf(allowedValues);
    }

    public static VariableValidation none() {
        return new VariableValidation(null, List.of(), null, null, null, null);
    }

    public static VariableValidation withRegex(String regex) {
        return new VariableValidation(Pattern.compile(regex), List.of(), null, null, null, null);
    }

    public static VariableValidation withAllowedValues(List<Object> allowedValues) {
        return new VariableValidation(null, allowedValues, null, null, null, null);
    }

    public static VariableValidation withLengthRange(int min, int max) {
        return new VariableValidation(null, List.of(), min, max, null, null);
    }

    public static VariableValidation withNumericRange(double min, double max) {
        return new VariableValidation(null, List.of(), null, null, min, max);
    }
}
