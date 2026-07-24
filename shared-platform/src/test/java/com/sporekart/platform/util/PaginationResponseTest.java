package com.sporekart.platform.util;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PaginationResponseTest {

    @Test
    void testOf() {
        List<String> items = List.of("a", "b", "c");
        PaginationResponse<String> response = PaginationResponse.of(items, 0, 10, 3);

        assertEquals(3, response.content().size());
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(3, response.totalElements());
        assertEquals(1, response.totalPages());
        assertTrue(response.first());
        assertTrue(response.last());
    }

    @Test
    void testEmpty() {
        PaginationResponse<?> response = PaginationResponse.empty();
        assertTrue(response.content().isEmpty());
        assertEquals(0, response.totalElements());
    }

    @Test
    void testMultiplePages() {
        List<String> items = List.of("a", "b");
        PaginationResponse<String> response = PaginationResponse.of(items, 1, 2, 10);

        assertEquals(2, response.content().size());
        assertEquals(1, response.page());
        assertEquals(5, response.totalPages());
        assertFalse(response.first());
        assertFalse(response.last());
    }
}
