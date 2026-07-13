package com.sporekart.ai.content.application;

import com.sporekart.ai.content.domain.ContentGenerationResponse;
import com.sporekart.ai.content.domain.ContentCategory;
import com.sporekart.ai.content.domain.ContentTone;
import com.sporekart.ai.content.domain.ModerationStatus;
import com.sporekart.ai.core.domain.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ContentSecurityServiceTest {

    private ContentSecurityService service;

    @BeforeEach
    void setUp() {
        service = new ContentSecurityService();
    }

    @Test
    void shouldValidateValidInput() {
        assertTrue(service.validateInput("Valid content text"));
    }

    @Test
    void shouldRejectNullInput() {
        assertFalse(service.validateInput(null));
    }

    @Test
    void shouldRejectBlankInput() {
        assertFalse(service.validateInput(""));
        assertFalse(service.validateInput("   "));
    }

    @Test
    void shouldRejectOversizedInput() {
        var oversized = "x".repeat(50001);
        assertFalse(service.validateInput(oversized));
    }

    @Test
    void shouldAcceptMaxLengthInput() {
        var maxSized = "x".repeat(50000);
        assertTrue(service.validateInput(maxSized));
    }

    @Test
    void shouldSanitizeContent() {
        var sanitized = service.sanitizeContent("Hello <script>alert('xss')</script>");
        assertFalse(sanitized.contains("<"));
        assertFalse(sanitized.contains(">"));
        assertFalse(sanitized.contains("'"));
    }

    @Test
    void shouldReturnNullWhenSanitizingNull() {
        assertNull(service.sanitizeContent(null));
    }

    @Test
    void shouldReturnEmptyWhenSanitizingBlank() {
        assertEquals("", service.sanitizeContent(""));
    }

    @Test
    void shouldReturnTrimmedContentWhenSanitizing() {
        var result = service.sanitizeContent("  hello world  ");
        assertEquals("hello world", result);
    }

    @Test
    void shouldAllowRequestWithinRateLimit() {
        var userId = UUID.randomUUID();
        assertTrue(service.checkRateLimit(userId));
    }

    @Test
    void shouldRejectNullUserIdForRateLimit() {
        assertFalse(service.checkRateLimit(null));
    }

    @Test
    void shouldRejectSuspendedUser() {
        var userId = UUID.randomUUID();
        service.suspendUser(userId);
        assertFalse(service.checkRateLimit(userId));
    }

    @Test
    void shouldReinstateUnsuspendedUser() {
        var userId = UUID.randomUUID();
        service.suspendUser(userId);
        service.unsuspendUser(userId);
        assertTrue(service.checkRateLimit(userId));
    }

    @Test
    void shouldRejectRequestWhenRateLimitExceeded() {
        var userId = UUID.randomUUID();
        IntStream.range(0, 100).forEach(i -> service.checkRateLimit(userId));
        assertFalse(service.checkRateLimit(userId));
    }

    @Test
    void shouldValidateValidOutput() {
        var response = new ContentGenerationResponse(UUID.randomUUID(), "req1", "Valid content",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.NEUTRAL,
                100, 0.9, false, ModerationStatus.APPROVED,
                OffsetDateTime.now(), 50L, true, null);
        assertTrue(service.validateOutput(response));
    }

    @Test
    void shouldRejectNullOutput() {
        assertFalse(service.validateOutput(null));
    }

    @Test
    void shouldRejectOutputWithBlankContent() {
        var response = new ContentGenerationResponse(UUID.randomUUID(), "req1", "",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.NEUTRAL,
                0, 0.0, false, ModerationStatus.PENDING,
                OffsetDateTime.now(), 0L, false, null);
        assertFalse(service.validateOutput(response));
    }

    @Test
    void shouldNotDetectPii() {
        assertFalse(service.containsPii("test content"));
    }

    @Test
    void shouldNotDetectProfanity() {
        assertFalse(service.containsProfanity("clean content"));
    }

    @Test
    void shouldSuspendUser() {
        var userId = UUID.randomUUID();
        service.suspendUser(userId);
        assertFalse(service.checkRateLimit(userId));
    }

    @Test
    void shouldNotFailWhenUnsuspendingNonSuspendedUser() {
        var userId = UUID.randomUUID();
        assertDoesNotThrow(() -> service.unsuspendUser(userId));
        assertTrue(service.checkRateLimit(userId));
    }
}
