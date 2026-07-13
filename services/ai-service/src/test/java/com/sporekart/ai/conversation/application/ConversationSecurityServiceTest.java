package com.sporekart.ai.conversation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversationSecurityServiceTest {

    private ConversationSecurityService securityService;

    @BeforeEach
    void setUp() {
        securityService = new ConversationSecurityService();
    }

    @Test
    void shouldAllowAccessWhenUserIsOwner() {
        assertTrue(securityService.canAccessSession("user-1", "user-1"));
    }

    @Test
    void shouldDenyAccessWhenUserIsNotOwner() {
        assertFalse(securityService.canAccessSession("user-1", "user-2"));
    }

    @Test
    void shouldDenyAccessWhenUserIdIsNull() {
        assertFalse(securityService.canAccessSession(null, "user-2"));
    }

    @Test
    void shouldDenyAccessWhenUserIdIsBlank() {
        assertFalse(securityService.canAccessSession("", "user-2"));
    }

    @Test
    void shouldDenyAccessForSuspendedUser() {
        securityService.suspendUser("user-1");
        assertFalse(securityService.canAccessSession("user-1", "user-1"));
    }

    @Test
    void shouldAllowAccessAfterUnsuspend() {
        securityService.suspendUser("user-1");
        securityService.unsuspendUser("user-1");
        assertTrue(securityService.canAccessSession("user-1", "user-1"));
    }

    @Test
    void shouldEnforceRateLimit() {
        for (int i = 0; i < 100; i++) {
            assertTrue(securityService.checkRateLimit("user-1"));
        }
        assertFalse(securityService.checkRateLimit("user-1"));
    }

    @Test
    void shouldReturnFalseForNullRateLimit() {
        assertFalse(securityService.checkRateLimit(null));
    }

    @Test
    void shouldResetRateLimit() {
        for (int i = 0; i < 100; i++) {
            securityService.checkRateLimit("user-1");
        }
        securityService.resetRateLimit("user-1");
        assertTrue(securityService.checkRateLimit("user-1"));
    }

    @Test
    void shouldSanitizeMessage() {
        var result = securityService.sanitizeMessage("<script>alert('xss')</script>");
        assertEquals("scriptalertxssscript", result);
    }

    @Test
    void shouldReturnNullForNullSanitize() {
        assertNull(securityService.sanitizeMessage(null));
    }

    @Test
    void shouldReturnBlankForBlankSanitize() {
        assertEquals("   ", securityService.sanitizeMessage("   "));
    }

    @Test
    void shouldValidateMessage() {
        assertTrue(securityService.validateMessage("Hello"));
    }

    @Test
    void shouldRejectNullMessage() {
        assertFalse(securityService.validateMessage(null));
    }

    @Test
    void shouldRejectBlankMessage() {
        assertFalse(securityService.validateMessage(""));
    }

    @Test
    void shouldRejectOversizedMessage() {
        var longMsg = "a".repeat(10001);
        assertFalse(securityService.validateMessage(longMsg));
    }
}
