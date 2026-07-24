package com.sporekart.platform.validation;

import com.sporekart.platform.error.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusinessValidatorTest {

    @Test
    void testValidPasses() {
        BusinessValidator.create()
                .requireNonBlank("hello", "name", "Name is required")
                .requirePositive(100, "price", "Price must be positive")
                .validate();
    }

    @Test
    void testNullValueFails() {
        assertThrows(ValidationException.class, () ->
                BusinessValidator.create()
                        .requireNonNull(null, "email", "Email is required")
                        .validate());
    }

    @Test
    void testBlankValueFails() {
        assertThrows(ValidationException.class, () ->
                BusinessValidator.create()
                        .requireNonBlank("", "name", "Name is required")
                        .validate());
    }

    @Test
    void testPositiveValueFails() {
        assertThrows(ValidationException.class, () ->
                BusinessValidator.create()
                        .requirePositive(-1, "qty", "Quantity must be positive")
                        .validate());
    }

    @Test
    void testMultipleErrors() {
        try {
            BusinessValidator.create()
                    .requireNonNull(null, "email", "Email is required")
                    .requireNonBlank("", "name", "Name is required")
                    .validate();
            fail("Expected ValidationException");
        } catch (ValidationException ex) {
            assertEquals(2, ex.getValidationErrors().size());
        }
    }

    @Test
    void testMaxLength() {
        assertThrows(ValidationException.class, () ->
                BusinessValidator.create()
                        .requireMaxLength("toolongvalue", "field", 5, "Too long")
                        .validate());
    }

    @Test
    void testMinLength() {
        assertThrows(ValidationException.class, () ->
                BusinessValidator.create()
                        .requireMinLength("ab", "field", 3, "Too short")
                        .validate());
    }

    @Test
    void testRange() {
        assertThrows(ValidationException.class, () ->
                BusinessValidator.create()
                        .requireRange(150, "age", 0, 120, "Age out of range")
                        .validate());
    }

    @Test
    void testHasErrors() {
        BusinessValidator validator = BusinessValidator.create();
        assertFalse(validator.hasErrors());
        validator.requireNonNull(null, "f", "err");
        assertTrue(validator.hasErrors());
    }

    @Test
    void testCheck() {
        assertThrows(ValidationException.class, () ->
                BusinessValidator.create()
                        .check(false, "condition", "Must be true")
                        .validate());
    }
}
