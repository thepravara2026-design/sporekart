package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.api.ContextRetrievalService;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContextRetrievalServiceImplTest {

    @Mock private SemanticEmbeddingRepository embeddingRepository;
    @Mock private SemanticRedisCacheService cacheService;

    private ContextRetrievalServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContextRetrievalServiceImpl(embeddingRepository, cacheService);
    }

    private SemanticEmbeddingEntity createEmbedding(String content) {
        SemanticEmbeddingEntity e = new SemanticEmbeddingEntity();
        e.setId(UUID.randomUUID());
        e.setContent(content);
        e.setStatus("COMPLETED");
        e.setProvider("OPENAI");
        e.setCreatedAt(OffsetDateTime.now());
        return e;
    }

    @Test
    void testRetrieveContextReturnsResults() {
        when(cacheService.getCachedContextResult(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(
                createEmbedding("how to grow tomatoes in greenhouse"),
                createEmbedding("tomato plant care guide"),
                createEmbedding("irrelevant content")
        ));

        ContextRetrievalService.ContextResult result = service.retrieveContext("tomato", Map.of(), 5);

        assertNotNull(result);
        assertEquals("tomato", result.query());
        assertTrue(result.totalResults() > 0);
        assertTrue(result.results().stream().allMatch(r -> r.content().toLowerCase().contains("tomato")));
    }

    @Test
    void testRetrieveContextReturnsEmptyForNoMatch() {
        when(cacheService.getCachedContextResult(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(
                createEmbedding("unrelated content")
        ));

        ContextRetrievalService.ContextResult result = service.retrieveContext("tomato", Map.of(), 5);

        assertEquals(0, result.totalResults());
    }

    @Test
    void testRetrieveContextRespectsMaxResults() {
        when(cacheService.getCachedContextResult(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(
                createEmbedding("tomato A"),
                createEmbedding("tomato B"),
                createEmbedding("tomato C")
        ));

        ContextRetrievalService.ContextResult result = service.retrieveContext("tomato", Map.of(), 2);

        assertTrue(result.results().size() <= 2);
    }

    @Test
    void testRetrieveContextUsesCache() {
        ContextRetrievalService.ContextResult cached = new ContextRetrievalService.ContextResult("test", List.of(), 0);
        when(cacheService.getCachedContextResult(anyString())).thenReturn(cached);

        ContextRetrievalService.ContextResult result = service.retrieveContext("test", Map.of(), 5);

        assertNotNull(result);
        verify(embeddingRepository, never()).findByIsDeletedFalse();
    }

    @Test
    void testRetrieveContextHandlesNullFilters() {
        when(cacheService.getCachedContextResult(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(
                createEmbedding("test content")
        ));

        ContextRetrievalService.ContextResult result = service.retrieveContext("test", null, 5);

        assertNotNull(result);
        assertTrue(result.totalResults() > 0);
    }

    @Test
    void testRetrieveContextHandlesNullQuery() {
        ContextRetrievalService.ContextResult result = service.retrieveContext(null, Map.of(), 5);

        assertNotNull(result);
        assertEquals(0, result.totalResults());
    }
}
