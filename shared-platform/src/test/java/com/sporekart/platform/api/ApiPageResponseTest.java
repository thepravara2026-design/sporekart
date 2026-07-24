package com.sporekart.platform.api;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ApiPageResponseTest {

    @Test
    void testOf() {
        List<String> items = List.of("a", "b", "c");
        ApiPageResponse<String> response = ApiPageResponse.of(items, 0, 10, 3);

        assertTrue(response.success());
        assertEquals(3, response.data().size());
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(3, response.totalElements());
        assertEquals(1, response.totalPages());
        assertTrue(response.first());
        assertTrue(response.last());
    }

    @Test
    void testPagination() {
        List<String> items = List.of("a", "b", "c", "d", "e");
        ApiPageResponse<String> response = ApiPageResponse.of(items, 1, 2, 5);

        assertEquals(1, response.page());
        assertEquals(2, response.size());
        assertEquals(5, response.totalElements());
        assertEquals(3, response.totalPages());
        assertFalse(response.first());
        assertFalse(response.last());
    }
}
