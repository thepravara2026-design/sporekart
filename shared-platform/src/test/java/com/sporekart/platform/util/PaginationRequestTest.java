package com.sporekart.platform.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaginationRequestTest {

    @Test
    void testDefaults() {
        PaginationRequest req = new PaginationRequest(0, 20, null, null);
        assertEquals(0, req.page());
        assertEquals(20, req.size());
        assertEquals("", req.sort());
        assertEquals("asc", req.direction());
    }

    @Test
    void testNegativePage() {
        PaginationRequest req = new PaginationRequest(-1, 20, null, null);
        assertEquals(0, req.page());
    }

    @Test
    void testMaxSize() {
        PaginationRequest req = new PaginationRequest(0, 2000, null, null);
        assertEquals(20, req.size());
    }

    @Test
    void testGetOffset() {
        PaginationRequest req = new PaginationRequest(2, 10, null, null);
        assertEquals(20, req.getOffset());
    }

    @Test
    void testIsAscending() {
        assertTrue(new PaginationRequest(0, 10, null, "asc").isAscending());
        assertFalse(new PaginationRequest(0, 10, null, "desc").isAscending());
    }

    @Test
    void testOf() {
        PaginationRequest req = PaginationRequest.of(1, 50);
        assertEquals(1, req.page());
        assertEquals(50, req.size());
    }

    @Test
    void testDefaultRequest() {
        PaginationRequest req = PaginationRequest.defaultRequest();
        assertEquals(0, req.page());
        assertEquals(20, req.size());
    }
}
