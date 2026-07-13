package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SearchType;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticSearchHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SemanticSearchServiceImplTest {

    @Mock private SemanticEmbeddingRepository embeddingRepository;
    @Mock private SemanticSearchHistoryRepository searchHistoryRepository;
    @Mock private SemanticRedisCacheService cacheService;
    @Mock private SemanticKafkaEventPublisher kafkaPublisher;

    private SemanticSearchServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SemanticSearchServiceImpl(embeddingRepository, searchHistoryRepository, cacheService, kafkaPublisher);
    }

    private SemanticEmbeddingEntity createEmbedding(String content) {
        SemanticEmbeddingEntity e = new SemanticEmbeddingEntity();
        e.setId(UUID.randomUUID());
        e.setContent(content);
        e.setProvider("OPENAI");
        e.setModel("text-embedding-3-small");
        e.setStatus("COMPLETED");
        return e;
    }

    @Test
    void testSearchReturnsResults() {
        when(cacheService.getCachedSearchResults(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(
                createEmbedding("machine learning guide"),
                createEmbedding("deep learning tutorial"),
                createEmbedding("cooking recipes")
        ));

        List<SemanticSearchResult> results = service.search("learning", SearchType.SEMANTIC, Map.of(), 10, 0.0);

        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(r -> r.content().contains("learning")));
    }

    @Test
    void testSearchAppliesThreshold() {
        when(cacheService.getCachedSearchResults(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(
                createEmbedding("machine learning guide")
        ));

        List<SemanticSearchResult> results = service.search("deep learning", SearchType.SEMANTIC, Map.of(), 10, 0.99);

        assertTrue(results.isEmpty());
    }

    @Test
    void testSearchRespectsLimit() {
        when(cacheService.getCachedSearchResults(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(
                createEmbedding("machine learning A"),
                createEmbedding("machine learning B"),
                createEmbedding("machine learning C")
        ));

        List<SemanticSearchResult> results = service.search("machine", SearchType.SEMANTIC, Map.of(), 2, 0.0);

        assertEquals(2, results.size());
    }

    @Test
    void testSearchUsesCache() {
        List<SemanticSearchResult> cached = List.of(
                new SemanticSearchResult("1", "cached", 1.0, 1, Map.of()));
        when(cacheService.getCachedSearchResults(anyString())).thenReturn(cached);

        List<SemanticSearchResult> results = service.search("test", SearchType.SEMANTIC, Map.of(), 10, 0.7);

        assertEquals(1, results.size());
        verify(embeddingRepository, never()).findByIsDeletedFalse();
    }

    @Test
    void testSimilaritySearch() {
        when(cacheService.getCachedSimilarityResults(anyString())).thenReturn(List.of());
        List<SemanticSearchResult> results = service.similaritySearch("emb-id", 10, 0.7);
        assertNotNull(results);
    }

    @Test
    void testHybridSearch() {
        when(cacheService.getCachedSearchResults(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(createEmbedding("test content")));

        List<SemanticSearchResult> results = service.hybridSearch("test", Map.of(), 10, 0.0);

        assertFalse(results.isEmpty());
    }

    @Test
    void testContextSearch() {
        when(cacheService.getCachedSearchResults(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(createEmbedding("context data")));

        List<SemanticSearchResult> results = service.contextSearch("context", Map.of());

        assertFalse(results.isEmpty());
    }

    @Test
    void testSearchReturnsEmptyForNoMatch() {
        when(cacheService.getCachedSearchResults(anyString())).thenReturn(null);
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(createEmbedding("irrelevant")));

        List<SemanticSearchResult> results = service.search("query", SearchType.SEMANTIC, Map.of(), 10, 0.0);

        assertTrue(results.isEmpty());
    }
}
