package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HybridScorerTest {

    private HybridScorer scorer;

    @BeforeEach
    void setUp() {
        scorer = new HybridScorer();
    }

    @Test
    void testFuse() {
        List<SemanticSearchResult> semantic = List.of(
                new SemanticSearchResult("1", "doc1", 0.9, 1, Map.of()),
                new SemanticSearchResult("2", "doc2", 0.8, 2, Map.of()));
        List<SemanticSearchResult> keyword = List.of(
                new SemanticSearchResult("3", "doc3", 0.7, 1, Map.of()),
                new SemanticSearchResult("1", "doc1", 0.6, 2, Map.of()));

        List<SemanticSearchResult> fused = scorer.fuse(semantic, keyword, 0.7, 0.3, 60);

        assertNotNull(fused);
        assertFalse(fused.isEmpty());
        assertTrue(fused.get(0).score() > 0);
    }

    @Test
    void testFuseWithEmptySemantic() {
        List<SemanticSearchResult> semantic = List.of();
        List<SemanticSearchResult> keyword = List.of(
                new SemanticSearchResult("1", "doc1", 0.7, 1, Map.of()));

        List<SemanticSearchResult> fused = scorer.fuse(semantic, keyword, 0.7, 0.3, 60);

        assertFalse(fused.isEmpty());
        assertEquals("doc1", fused.get(0).content());
    }

    @Test
    void testWeightedFusion() {
        List<SemanticSearchResult> semantic = List.of(
                new SemanticSearchResult("1", "doc1", 0.8, 1, Map.of()));
        List<SemanticSearchResult> keyword = List.of(
                new SemanticSearchResult("1", "doc1", 0.6, 1, Map.of()));

        List<SemanticSearchResult> fused = scorer.weightedFusion(semantic, keyword, 0.7, 0.3);

        assertEquals(1, fused.size());
        assertEquals(0.74, fused.get(0).score(), 0.001);
    }

    @Test
    void testWeightedFusionDisjoint() {
        List<SemanticSearchResult> semantic = List.of(
                new SemanticSearchResult("1", "doc1", 0.9, 1, Map.of()));
        List<SemanticSearchResult> keyword = List.of(
                new SemanticSearchResult("2", "doc2", 0.8, 1, Map.of()));

        List<SemanticSearchResult> fused = scorer.weightedFusion(semantic, keyword, 0.7, 0.3);

        assertEquals(2, fused.size());
    }
}
