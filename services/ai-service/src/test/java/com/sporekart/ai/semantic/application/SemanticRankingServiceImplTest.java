package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.RankingStrategy;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SemanticRankingServiceImplTest {

    private SemanticRankingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SemanticRankingServiceImpl();
    }

    private List<SemanticSearchResult> createResults() {
        return List.of(
                new SemanticSearchResult("1", "low score", 0.3, 3, Map.of("type", "guide")),
                new SemanticSearchResult("2", "high score", 0.9, 1, Map.of("type", "tutorial")),
                new SemanticSearchResult("3", "medium score", 0.6, 2, Map.of("type", "reference"))
        );
    }

    @Test
    void testRankBySimilarity() {
        List<SemanticSearchResult> ranked = service.rank(createResults(), RankingStrategy.SIMILARITY, Map.of());
        assertEquals(0.9, ranked.get(0).score());
        assertEquals(0.3, ranked.get(2).score());
    }

    @Test
    void testRankEmptyResults() {
        List<SemanticSearchResult> ranked = service.rank(List.of(), RankingStrategy.SIMILARITY, Map.of());
        assertTrue(ranked.isEmpty());
    }

    @Test
    void testRankNullResults() {
        List<SemanticSearchResult> ranked = service.rank(null, RankingStrategy.SIMILARITY, Map.of());
        assertTrue(ranked.isEmpty());
    }

    @Test
    void testRankByFreshness() {
        List<SemanticSearchResult> ranked = service.rank(createResults(), RankingStrategy.FRESHNESS, Map.of());
        assertNotNull(ranked);
        assertEquals(3, ranked.size());
    }

    @Test
    void testRankByHybrid() {
        List<SemanticSearchResult> ranked = service.rank(createResults(), RankingStrategy.HYBRID,
                Map.of("semanticWeight", 0.7, "keywordWeight", 0.3));
        assertNotNull(ranked);
        assertEquals(3, ranked.size());
    }

    @Test
    void testRankByBusinessPriority() {
        List<SemanticSearchResult> ranked = service.rank(createResults(), RankingStrategy.BUSINESS_PRIORITY, Map.of());
        assertNotNull(ranked);
        assertEquals(3, ranked.size());
    }

    @Test
    void testRankByKeywordWeight() {
        List<SemanticSearchResult> ranked = service.rank(createResults(), RankingStrategy.KEYWORD_WEIGHT,
                Map.of("keywordWeight", 0.5));
        assertNotNull(ranked);
        assertEquals(3, ranked.size());
    }

    @Test
    void testRankByMetadataBoost() {
        List<SemanticSearchResult> ranked = service.rank(createResults(), RankingStrategy.METADATA_BOOST,
                Map.of("boostKey", "type"));
        assertNotNull(ranked);
        assertEquals(3, ranked.size());
    }
}
