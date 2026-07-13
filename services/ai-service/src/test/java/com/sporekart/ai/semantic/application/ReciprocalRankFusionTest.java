package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReciprocalRankFusionTest {

    private ReciprocalRankFusion fusion;

    @BeforeEach
    void setUp() {
        fusion = new ReciprocalRankFusion();
    }

    @Test
    void testFuseSingleList() {
        List<SemanticSearchResult> list = List.of(
                new SemanticSearchResult("1", "doc1", 0.9, 1, Map.of()),
                new SemanticSearchResult("2", "doc2", 0.8, 2, Map.of()));

        List<SemanticSearchResult> fused = fusion.fuse(List.of(list));

        assertEquals(2, fused.size());
        assertTrue(fused.get(0).score() > fused.get(1).score());
    }

    @Test
    void testFuseMultipleLists() {
        List<SemanticSearchResult> list1 = List.of(
                new SemanticSearchResult("1", "doc1", 0.9, 1, Map.of()),
                new SemanticSearchResult("2", "doc2", 0.8, 2, Map.of()));
        List<SemanticSearchResult> list2 = List.of(
                new SemanticSearchResult("2", "doc2", 0.7, 1, Map.of()),
                new SemanticSearchResult("3", "doc3", 0.6, 2, Map.of()));

        List<SemanticSearchResult> fused = fusion.fuse(List.of(list1, list2));

        assertEquals(3, fused.size());
    }

    @Test
    void testFuseWithCustomK() {
        List<SemanticSearchResult> list = List.of(
                new SemanticSearchResult("1", "doc1", 0.9, 1, Map.of()));

        List<SemanticSearchResult> fused = fusion.fuse(List.of(list), 100);

        assertEquals(1, fused.size());
        assertEquals(1.0 / 101.0, fused.get(0).score(), 0.0001);
    }

    @Test
    void testFuseEmptyLists() {
        List<SemanticSearchResult> fused = fusion.fuse(List.of());
        assertTrue(fused.isEmpty());
    }

    @Test
    void testFuseNullLists() {
        List<SemanticSearchResult> fused = fusion.fuse(null);
        assertTrue(fused.isEmpty());
    }
}
