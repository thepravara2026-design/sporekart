package com.sporekart.platform.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testBuilder() {
        ErrorResponse response = ErrorResponse.builder()
                .message("Order not found")
                .status(404)
                .errorCode("NOT_FOUND")
                .path("/api/orders/ord-123")
                .traceId("trace-1")
                .build();

        assertFalse(response.success());
        assertEquals(404, response.status());
        assertEquals("NOT_FOUND", response.errorCode());
    }

    @Test
    void testFromException() {
        SporekartException ex = new NotFoundException("Order", "ord-123");
        ErrorResponse response = ErrorResponse.fromException(ex, "/api/orders/ord-123", "trace-1");

        assertFalse(response.success());
        assertEquals(404, response.status());
    }
}
