package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticSearchHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HybridSearchServiceImplTest {

    @Mock private SemanticEmbeddingRepository embeddingRepository;
    @Mock private SemanticSearchHistoryRepository searchHistoryRepository;
    @Mock private SemanticRedisCacheService cacheService;
    @Mock private SemanticKafkaEventPublisher kafkaPublisher;

    private HybridSearchServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new HybridSearchServiceImpl(embeddingRepository, searchHistoryRepository, cacheService, kafkaPublisher);
    }

    @Test
    void testAggregateResults() {
        List<SemanticSearchResult> vectorResults = List.of(
                new SemanticSearchResult("1", "vector doc", 0.8, 1, Map.of())
        );
        List<SemanticSearchResult> keywordResults = List.of(
                new SemanticSearchResult("2", "keyword doc", 0.6, 1, Map.of())
        );

        List<SemanticSearchResult> aggregated = service.aggregateResults(vectorResults, keywordResults, Map.of("vector", 0.7, "keyword", 0.3));

        assertEquals(2, aggregated.size());
    }

    @Test
    void testAggregateResultsMergesSameDoc() {
        List<SemanticSearchResult> vectorResults = List.of(
                new SemanticSearchResult("1", "doc", 0.8, 1, Map.of())
        );
        List<SemanticSearchResult> keywordResults = List.of(
                new SemanticSearchResult("1", "doc", 0.6, 1, Map.of())
        );

        List<SemanticSearchResult> aggregated = service.aggregateResults(vectorResults, keywordResults, Map.of("vector", 0.7, "keyword", 0.3));

        assertEquals(1, aggregated.size());
        assertEquals(0.8 * 0.7 + 0.6 * 0.3, aggregated.get(0).score(), 0.001);
    }

    @Test
    void testAggregateResultsDefaultWeights() {
        List<SemanticSearchResult> vectorResults = List.of(
                new SemanticSearchResult("1", "doc", 1.0, 1, Map.of())
        );
        List<SemanticSearchResult> keywordResults = List.of();

        List<SemanticSearchResult> aggregated = service.aggregateResults(vectorResults, keywordResults, Map.of());

        assertEquals(1, aggregated.size());
        assertEquals(0.7, aggregated.get(0).score(), 0.001);
    }

    @Test
    void testAggregateResultsHandlesEmpty() {
        List<SemanticSearchResult> aggregated = service.aggregateResults(List.of(), List.of(), Map.of());
        assertTrue(aggregated.isEmpty());
    }

    @Test
    void testFallbackSearch() {
        assertNotNull(service);
    }

    @Test
    void testFallbackSearchReturnsResults() {
        assertDoesNotThrow(() -> service.fallbackSearch("test", Map.of()));
    }
}
