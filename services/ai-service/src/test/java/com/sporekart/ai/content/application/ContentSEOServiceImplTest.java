package com.sporekart.ai.content.application;

import com.sporekart.ai.core.domain.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContentSEOServiceImplTest {

    private ContentSEOServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContentSEOServiceImpl();
    }

    @Test
    void shouldGenerateSEO() {
        var request = new com.sporekart.ai.content.domain.ContentSEORequest(
                UUID.randomUUID(), "Content body for SEO optimization",
                "seo-keyword", "developers", ContentType.TEXT, "en");

        var result = service.generateSEO(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(request.id(), result.requestId());
        assertTrue(result.title().contains("seo-keyword"));
        assertFalse(result.metaDescription().isEmpty());
        assertTrue(result.keywords().contains("seo-keyword"));
        assertEquals("seo-keyword", result.slug());
        assertTrue(result.readabilityScore() > 0);
        assertTrue(result.seoScore() > 0);
        assertFalse(result.suggestions().isEmpty());
        assertNotNull(result.generatedAt());
    }

    @Test
    void shouldGenerateSEOWithNullKeyword() {
        var request = new com.sporekart.ai.content.domain.ContentSEORequest(
                UUID.randomUUID(), "Content", null, null, ContentType.TEXT, "en");

        var result = service.generateSEO(request);

        assertNotNull(result);
        assertTrue(result.title().contains("content"));
        assertTrue(result.keywords().contains("general"));
        assertEquals("content", result.slug());
    }

    @Test
    void shouldGenerateSEOWithKeywordContainingSpaces() {
        var request = new com.sporekart.ai.content.domain.ContentSEORequest(
                UUID.randomUUID(), "Content", "seo keyword test", "audience",
                ContentType.TEXT, "en");

        var result = service.generateSEO(request);

        assertNotNull(result);
        assertEquals("seo-keyword-test", result.slug());
    }

    @Test
    void shouldAnalyzeSEO() {
        var result = service.analyzeSEO("Content to analyze", "target-keyword");

        assertNotNull(result);
        assertNotNull(result.id());
        assertTrue(result.title().contains("target-keyword"));
        assertEquals("target-keyword", result.slug());
        assertTrue(result.seoScore() > 0);
        assertTrue(result.readabilityScore() > 0);
        assertFalse(result.suggestions().isEmpty());
    }

    @Test
    void shouldAnalyzeSEOWithNullKeyword() {
        var result = service.analyzeSEO("Content", null);

        assertNotNull(result);
        assertTrue(result.keywords().contains("general"));
        assertEquals("content", result.slug());
    }

    @Test
    void shouldGetSEOAnalysisReturnEmpty() {
        var id = UUID.randomUUID();

        var result = service.getSEOAnalysis(id);

        assertTrue(result.isEmpty());
    }
}
