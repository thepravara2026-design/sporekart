package com.sporekart.platform.validation;

import com.sporekart.platform.error.BusinessException;
import com.sporekart.platform.error.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BusinessValidator {

    private final List<ValidationException.ValidationError> errors = new ArrayList<>();

    public static BusinessValidator create() {
        return new BusinessValidator();
    }

    public BusinessValidator requireNonNull(Object value, String field, String message) {
        if (value == null) {
            errors.add(new ValidationException.ValidationError(field, message));
        }
        return this;
    }

    public BusinessValidator requireNonBlank(String value, String field, String message) {
        if (value == null || value.isBlank()) {
            errors.add(new ValidationException.ValidationError(field, message));
        }
        return this;
    }

    public BusinessValidator requirePositive(Number value, String field, String message) {
        if (value == null || value.doubleValue() <= 0) {
            errors.add(new ValidationException.ValidationError(field, message));
        }
        return this;
    }

    public BusinessValidator requireTrue(boolean condition, String field, String message) {
        if (!condition) {
            errors.add(new ValidationException.ValidationError(field, message));
        }
        return this;
    }

    public BusinessValidator requireMaxLength(String value, String field, int max, String message) {
        if (value != null && value.length() > max) {
            errors.add(new ValidationException.ValidationError(field, message));
        }
        return this;
    }

    public BusinessValidator requireMinLength(String value, String field, int min, String message) {
        if (value != null && value.length() < min) {
            errors.add(new ValidationException.ValidationError(field, message));
        }
        return this;
    }

    public BusinessValidator requireRange(Number value, String field, Number min, Number max, String message) {
        if (value != null && (value.doubleValue() < min.doubleValue() || value.doubleValue() > max.doubleValue())) {
            errors.add(new ValidationException.ValidationError(field, message));
        }
        return this;
    }

    public BusinessValidator check(boolean condition, String field, String message) {
        if (!condition) {
            errors.add(new ValidationException.ValidationError(field, message));
        }
        return this;
    }

    public BusinessValidator check(boolean condition, String message) {
        if (!condition) {
            errors.add(new ValidationException.ValidationError("", message));
        }
        return this;
    }

    public void validate() {
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }
    }

    public void validate(String businessMessage) {
        if (!errors.isEmpty()) {
            throw new ValidationException(businessMessage, errors);
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ValidationException.ValidationError> getErrors() {
        return List.copyOf(errors);
    }
}
