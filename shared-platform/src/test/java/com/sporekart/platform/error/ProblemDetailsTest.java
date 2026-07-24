package com.sporekart.platform.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProblemDetailsTest {

    @Test
    void testBuilder() {
        ProblemDetails pd = ProblemDetails.builder()
                .type("https://api.sporekart.com/errors/not_found")
                .title("Order not found")
                .status(404)
                .detail("Order ord-123 not found")
                .instance("/api/orders/ord-123")
                .traceId("trace-1")
                .errorCode("NOT_FOUND")
                .build();

        assertEquals(404, pd.status());
        assertEquals("Order not found", pd.title());
        assertEquals("trace-1", pd.traceId());
    }

    @Test
    void testFromException() {
        SporekartException ex = new NotFoundException("Order", "ord-123");
        ProblemDetails pd = ProblemDetails.fromException(ex, "/api/orders/ord-123", "trace-1");

        assertEquals(404, pd.status());
        assertEquals("NOT_FOUND", pd.errorCode());
        assertTrue(pd.detail().contains("ord-123"));
    }
}
