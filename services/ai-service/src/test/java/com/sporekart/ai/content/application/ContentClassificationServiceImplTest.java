package com.sporekart.ai.content.application;

import com.sporekart.ai.content.domain.ContentClassificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContentClassificationServiceImplTest {

    private ContentClassificationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContentClassificationServiceImpl();
    }

    @Test
    void shouldClassify() {
        var request = new ContentClassificationRequest(UUID.randomUUID(),
                "This is a technology article about programming", List.of("tech", "science"), 3);

        var result = service.classify(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(request.id(), result.requestId());
        assertEquals("general", result.primaryCategory());
        assertFalse(result.classifications().isEmpty());
        assertEquals(1, result.classifications().size());
        assertTrue(result.confidenceScore() > 0);
        assertEquals(1, result.keywords().size());
        assertNotNull(result.generatedAt());
    }

    @Test
    void shouldClassifyWithKeywords() {
        var request = new ContentClassificationRequest(UUID.randomUUID(),
                "A sports article about football", List.of("sports", "football"), 5);

        var result = service.classifyWithKeywords(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(request.id(), result.requestId());
        assertEquals("general", result.primaryCategory());
        assertEquals(2, result.classifications().size());
        assertEquals(3, result.keywords().size());
        assertTrue(result.keywords().contains("general"));
        assertTrue(result.keywords().contains("text"));
    }

    @Test
    void shouldClassifyWithNullCategories() {
        var request = new ContentClassificationRequest(UUID.randomUUID(), "Simple text", null, 2);

        var result = service.classify(request);

        assertNotNull(result);
        assertEquals("general", result.primaryCategory());
    }

    @Test
    void shouldClassifyWithZeroMaxCategories() {
        var request = new ContentClassificationRequest(UUID.randomUUID(), "Text", List.of("cat1"), 0);

        var result = service.classify(request);

        assertNotNull(result);
        assertEquals("general", result.primaryCategory());
    }

    @Test
    void shouldClassifyWithKeywordsAndNullCategories() {
        var request = new ContentClassificationRequest(UUID.randomUUID(), "Text", null, 3);

        var result = service.classifyWithKeywords(request);

        assertNotNull(result);
        assertEquals(2, result.classifications().size());
    }

    @Test
    void shouldGetClassificationReturnEmpty() {
        var id = UUID.randomUUID();

        var result = service.getClassification(id);

        assertTrue(result.isEmpty());
    }
}
