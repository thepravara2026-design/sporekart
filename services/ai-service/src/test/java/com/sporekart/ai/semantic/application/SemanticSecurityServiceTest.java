package com.sporekart.ai.semantic.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SemanticSecurityServiceTest {

    private SemanticSecurityService securityService;

    @BeforeEach
    void setUp() {
        securityService = new SemanticSecurityService();
    }

    @Test
    void testCanAccessEmbeddingWithValidUser() {
        assertTrue(securityService.canAccessEmbedding("user-1", "PUBLIC"));
    }

    @Test
    void testCanAccessEmbeddingWithNullUser() {
        assertFalse(securityService.canAccessEmbedding(null, "PUBLIC"));
    }

    @Test
    void testCheckRateLimitAllowsRequests() {
        assertTrue(securityService.checkRateLimit("user-1"));
    }

    @Test
    void testCheckRateLimitWithNullUser() {
        assertFalse(securityService.checkRateLimit(null));
    }

    @Test
    void testSanitizeInputRemovesSpecialChars() {
        String result = securityService.sanitizeInput("<script>alert('xss')</script>");
        assertEquals("scriptalertxssscript", result);
    }

    @Test
    void testSanitizeInputWithNormalText() {
        String result = securityService.sanitizeInput("hello world");
        assertEquals("hello world", result);
    }

    @Test
    void testSanitizeInputWithNull() {
        assertNull(securityService.sanitizeInput(null));
    }

    @Test
    void testValidateQueryValid() {
        assertTrue(securityService.validateQuery("valid search query"));
    }

    @Test
    void testValidateQueryEmpty() {
        assertFalse(securityService.validateQuery(""));
    }

    @Test
    void testValidateQueryNull() {
        assertFalse(securityService.validateQuery(null));
    }

    @Test
    void testValidateQueryTooLong() {
        String longQuery = "a".repeat(1001);
        assertFalse(securityService.validateQuery(longQuery));
    }
}
