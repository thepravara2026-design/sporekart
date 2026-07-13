package com.sporekart.ai.content.application;

import com.sporekart.ai.content.domain.ContentSummaryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContentSummaryServiceImplTest {

    private ContentSummaryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContentSummaryServiceImpl();
    }

    @Test
    void shouldSummarize() {
        var request = new ContentSummaryRequest(UUID.randomUUID(),
                "This is a long text that needs to be summarized into a shorter version.",
                50, "en", false);

        var result = service.summarize(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(request.id(), result.requestId());
        assertTrue(result.summary().contains("Stub summary of:"));
        assertEquals(request.sourceText().length(), result.originalLength());
        assertEquals(50, result.summaryLength());
        assertEquals(0.5, result.compressionRatio());
        assertEquals("en", result.language());
        assertNotNull(result.generatedAt());
    }

    @Test
    void shouldSummarizeWithNullLanguage() {
        var request = new ContentSummaryRequest(UUID.randomUUID(), "Short text", 50, null, false);

        var result = service.summarize(request);

        assertNotNull(result);
        assertEquals("en", result.language());
    }

    @Test
    void shouldSummarizeWithVeryShortText() {
        var request = new ContentSummaryRequest(UUID.randomUUID(), "Hi", 50, "fr", true);

        var result = service.summarize(request);

        assertNotNull(result);
        assertTrue(result.summary().contains("Hi"));
        assertEquals("fr", result.language());
    }

    @Test
    void shouldSummarizeWithKeyPoints() {
        var request = new ContentSummaryRequest(UUID.randomUUID(),
                "This is a long text that needs key points extracted from it.", 50, "en", true);

        var result = service.summarizeWithKeyPoints(request);

        assertNotNull(result);
        assertTrue(result.summary().contains("Stub summary with key points"));
        assertEquals(request.id(), result.requestId());
    }

    @Test
    void shouldSummarizeWithKeyPointsUsingNullLanguage() {
        var request = new ContentSummaryRequest(UUID.randomUUID(), "Text", 50, null, true);

        var result = service.summarizeWithKeyPoints(request);

        assertNotNull(result);
        assertEquals("en", result.language());
    }

    @Test
    void shouldGetSummaryReturnEmpty() {
        var id = UUID.randomUUID();

        var result = service.getSummary(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleEmptySourceText() {
        var request = new ContentSummaryRequest(UUID.randomUUID(), "", 50, "en", false);

        var result = service.summarize(request);

        assertNotNull(result);
        assertEquals(0, result.originalLength());
    }

    @Test
    void shouldHandleNullSourceTextInSummary() {
        var request = new ContentSummaryRequest(UUID.randomUUID(), "Text", 100, "en", false);

        var result = service.summarize(request);
        assertNotNull(result.generatedAt());
        assertTrue(result.generatedAt().isBefore(OffsetDateTime.now().plusSeconds(1)));
    }
}
