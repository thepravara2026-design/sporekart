package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CrossEncoderScorerTest {

    private CrossEncoderScorer scorer;

    @BeforeEach
    void setUp() {
        scorer = new CrossEncoderScorer();
    }

    @Test
    void testRerank() {
        List<SemanticSearchResult> candidates = List.of(
                new SemanticSearchResult("1", "machine learning tutorial", 0.5, 1, Map.of()),
                new SemanticSearchResult("2", "deep learning guide", 0.4, 2, Map.of()));

        List<SemanticSearchResult> reranked = scorer.rerank(candidates, "machine learning");

        assertNotNull(reranked);
        assertEquals(2, reranked.size());
    }

    @Test
    void testRerankEmpty() {
        List<SemanticSearchResult> reranked = scorer.rerank(List.of(), "query");
        assertTrue(reranked.isEmpty());
    }

    @Test
    void testRerankTopN() {
        List<SemanticSearchResult> candidates = List.of(
                new SemanticSearchResult("1", "document one", 0.6, 1, Map.of()),
                new SemanticSearchResult("2", "document two", 0.5, 2, Map.of()),
                new SemanticSearchResult("3", "document three", 0.4, 3, Map.of()));

        List<SemanticSearchResult> reranked = scorer.rerankTopN(candidates, "document", 2);

        assertEquals(2, reranked.size());
    }
}
