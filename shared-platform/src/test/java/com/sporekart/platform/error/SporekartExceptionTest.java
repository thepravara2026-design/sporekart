package com.sporekart.platform.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SporekartExceptionTest {

    @Test
    void testBusinessException() {
        BusinessException ex = new BusinessException("Order cannot be cancelled");
        assertEquals(ErrorCode.BUSINESS_RULE_VIOLATION, ex.getErrorCode());
        assertEquals("Order cannot be cancelled", ex.getMessage());
        assertEquals(422, ex.getHttpStatus());
    }

    @Test
    void testNotFoundException() {
        NotFoundException ex = new NotFoundException("Order", "ord-123");
        assertEquals(ErrorCode.NOT_FOUND, ex.getErrorCode());
        assertEquals("Order not found: ord-123", ex.getMessage());
        assertEquals(404, ex.getHttpStatus());
    }

    @Test
    void testValidationException() {
        ValidationException ex = new ValidationException("Invalid input");
        assertEquals(ErrorCode.VALIDATION_ERROR, ex.getErrorCode());
        assertEquals(400, ex.getHttpStatus());
    }

    @Test
    void testValidationExceptionWithErrors() {
        var errors = java.util.List.of(
                new ValidationException.ValidationError("email", "Email is required"));
        ValidationException ex = new ValidationException("Validation failed", errors);
        assertEquals(1, ex.getValidationErrors().size());
        assertEquals("email", ex.getValidationErrors().get(0).field());
    }

    @Test
    void testDuplicateResourceException() {
        DuplicateResourceException ex = new DuplicateResourceException("Product", "sku", "SKU-123");
        assertEquals(ErrorCode.DUPLICATE_RESOURCE, ex.getErrorCode());
        assertEquals(409, ex.getHttpStatus());
    }

    @Test
    void testSecurityException() {
        SecurityException ex = new SecurityException("Unauthorized");
        assertEquals(ErrorCode.UNAUTHORIZED, ex.getErrorCode());
        assertEquals(401, ex.getHttpStatus());
    }

    @Test
    void testInfrastructureException() {
        InfrastructureException ex = new InfrastructureException("Database connection failed");
        assertEquals(ErrorCode.INTERNAL_ERROR, ex.getErrorCode());
        assertEquals(500, ex.getHttpStatus());
    }
}
