package com.sporekart.platform.api;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void testOk() {
        ApiResponse<String> response = ApiResponse.ok("test data");
        assertTrue(response.success());
        assertEquals("Success", response.message());
        assertEquals("test data", response.data());
    }

    @Test
    void testCreated() {
        ApiResponse<Map<String, String>> response = ApiResponse.created(Map.of("id", "abc"));
        assertTrue(response.success());
        assertEquals("Created", response.message());
    }

    @Test
    void testEmpty() {
        ApiResponse<?> response = ApiResponse.empty();
        assertTrue(response.success());
        assertNull(response.data());
    }

    @Test
    void testOkWithMessage() {
        ApiResponse<String> response = ApiResponse.ok("Custom message", "data");
        assertEquals("Custom message", response.message());
    }
}
