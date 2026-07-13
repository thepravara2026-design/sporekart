package com.sporekart.ai.content.application;

import com.sporekart.ai.content.domain.ContentTranslationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContentTranslationServiceImplTest {

    private ContentTranslationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContentTranslationServiceImpl();
    }

    @Test
    void shouldTranslate() {
        var request = new ContentTranslationRequest(UUID.randomUUID(), "Hello world", "en", "fr", false, null);

        var result = service.translate(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(request.id(), result.requestId());
        assertEquals("Hello world", result.translatedText());
        assertEquals("en", result.sourceLanguage());
        assertEquals("fr", result.targetLanguage());
        assertEquals("en", result.detectedLanguage());
        assertTrue(result.confidenceScore() > 0);
        assertNotNull(result.generatedAt());
    }

    @Test
    void shouldTranslateWithNullSourceLanguage() {
        var request = new ContentTranslationRequest(UUID.randomUUID(), "Hello", null, "es", true, null);

        var result = service.translate(request);

        assertNotNull(result);
        assertEquals("es", result.targetLanguage());
        assertEquals("en", result.detectedLanguage());
        assertEquals("en", result.sourceLanguage());
    }

    @Test
    void shouldTranslateWithPreserveFormatting() {
        var request = new ContentTranslationRequest(UUID.randomUUID(), "Line1\nLine2", "en", "de", true, null);

        var result = service.translate(request);

        assertNotNull(result);
        assertEquals("de", result.targetLanguage());
        assertEquals("en", result.sourceLanguage());
    }

    @Test
    void shouldDetectLanguage() {
        var language = service.detectLanguage("Hello world");

        assertEquals("en", language);
    }

    @Test
    void shouldDetectLanguageForEmptyText() {
        var language = service.detectLanguage("");

        assertEquals("en", language);
    }

    @Test
    void shouldDetectLanguageForNullText() {
        var language = service.detectLanguage(null);

        assertEquals("en", language);
    }

    @Test
    void shouldTranslateWithGlossary() {
        var request = new ContentTranslationRequest(UUID.randomUUID(), "Hello", "en", "fr", false,
                Map.of("Hello", "Bonjour"));

        var result = service.translateWithGlossary(request);

        assertNotNull(result);
        assertEquals(request.id(), result.requestId());
        assertEquals("fr", result.targetLanguage());
        assertEquals("en", result.detectedLanguage());
    }

    @Test
    void shouldTranslateWithGlossaryAndNullSourceLanguage() {
        var request = new ContentTranslationRequest(UUID.randomUUID(), "Hello", null, "de", false, null);

        var result = service.translateWithGlossary(request);

        assertNotNull(result);
        assertEquals("de", result.targetLanguage());
        assertEquals("en", result.sourceLanguage());
    }

    @Test
    void shouldGetTranslationReturnEmpty() {
        var id = UUID.randomUUID();

        var result = service.getTranslation(id);

        assertTrue(result.isEmpty());
    }
}
