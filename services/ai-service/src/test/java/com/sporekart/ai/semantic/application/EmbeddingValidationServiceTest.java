package com.sporekart.ai.semantic.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingValidationServiceTest {

    private EmbeddingValidationService service;

    @BeforeEach
    void setUp() {
        service = new EmbeddingValidationService();
    }

    @Test
    void testValidateContentRejectsNull() {
        assertFalse(service.validateContent(null));
    }

    @Test
    void testValidateContentRejectsBlank() {
        assertFalse(service.validateContent(""));
        assertFalse(service.validateContent("   "));
    }

    @Test
    void testValidateContentAcceptsValid() {
        assertTrue(service.validateContent("valid content"));
    }

    @Test
    void testValidateEmbeddingRejectsNull() {
        assertFalse(service.validate(null, 768));
    }

    @Test
    void testValidateEmbeddingRejectsEmpty() {
        assertFalse(service.validate(List.of(), 768));
    }

    @Test
    void testValidateEmbeddingChecksDimensions() {
        assertFalse(service.validate(List.of(0.1, 0.2, 0.3), 768));
    }

    @Test
    void testValidateEmbeddingAcceptsCorrectDimensions() {
        List<Double> embedding = List.of(0.1, 0.2, 0.3);
        assertTrue(service.validate(embedding, 3));
    }

    @Test
    void testValidateEmbeddingAcceptsZeroDimensionsCheck() {
        assertTrue(service.validate(List.of(0.1, 0.2), 0));
    }

    @Test
    void testValidateContentRejectsTooLong() {
        String longContent = "a".repeat(100001);
        assertFalse(service.validateContent(longContent));
    }

    @Test
    void testValidateContentRejectsTooShort() {
        assertFalse(service.validateContent(""));
    }
}
