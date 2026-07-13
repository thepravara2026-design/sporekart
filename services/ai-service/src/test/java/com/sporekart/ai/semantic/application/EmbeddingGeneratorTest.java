package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.infrastructure.adapters.GeminiEmbeddingAdapter;
import com.sporekart.ai.semantic.infrastructure.adapters.OpenAIEmbeddingAdapter;
import com.sporekart.ai.semantic.infrastructure.adapters.ProviderRoutingEmbeddingGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingGeneratorTest {

    @Mock private OpenAIEmbeddingAdapter openAIAdapter;
    @Mock private GeminiEmbeddingAdapter geminiAdapter;

    private ProviderRoutingEmbeddingGenerator generator;

    @BeforeEach
    void setUp() {
        lenient().when(openAIAdapter.isAvailable()).thenReturn(true);
        lenient().when(geminiAdapter.isAvailable()).thenReturn(true);
        generator = new ProviderRoutingEmbeddingGenerator(openAIAdapter, geminiAdapter);
        generator.registerAdapters();
    }

    @Test
    void testGenerateWithOpenAI() {
        when(openAIAdapter.generateWithModel("test content", "text-embedding-3-small"))
                .thenReturn(List.of(0.1, 0.2, 0.3));

        List<Double> result = generator.generate("test content", "OPENAI", "text-embedding-3-small");

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(openAIAdapter).generateWithModel("test content", "text-embedding-3-small");
    }

    @Test
    void testGenerateWithGemini() {
        when(geminiAdapter.generate("gemini content")).thenReturn(List.of(0.5, 0.6, 0.7, 0.8));

        List<Double> result = generator.generate("gemini content", "GEMINI", "embedding-001");

        assertNotNull(result);
        assertEquals(4, result.size());
        verify(geminiAdapter).generate("gemini content");
    }

    @Test
    void testGenerateBatch() {
        when(openAIAdapter.generateBatch(anyList()))
                .thenReturn(List.of(List.of(0.1, 0.2), List.of(0.3, 0.4)));

        List<List<Double>> results = generator.generateBatch(List.of("a", "b"), "OPENAI");

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(openAIAdapter).generateBatch(List.of("a", "b"));
    }

    @Test
    void testGenerateBatchEmpty() {
        List<List<Double>> results = generator.generateBatch(List.of(), "OPENAI");
        assertTrue(results.isEmpty());
    }

    @Test
    void testGenerateThrowsOnUnknownProvider() {
        assertThrows(EmbeddingException.class,
                () -> generator.generate("content", "UNKNOWN", "model"));
    }

    @Test
    void testGenerateThrowsOnUnavailableProvider() {
        when(openAIAdapter.isAvailable()).thenReturn(false);
        assertThrows(EmbeddingException.class,
                () -> generator.generate("content", "OPENAI", "model"));
    }
}
