package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSimilarityScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimilarityScorerTest {

    private SimilarityScorer scorer;

    @BeforeEach
    void setUp() {
        scorer = new SimilarityScorer();
    }

    @Test
    void testCosineSimilarity() {
        List<Double> a = List.of(1.0, 0.0, 0.0);
        List<Double> b = List.of(1.0, 0.0, 0.0);
        assertEquals(1.0, scorer.cosine(a, b), 0.0001);
    }

    @Test
    void testCosineSimilarityOrthogonal() {
        List<Double> a = List.of(1.0, 0.0);
        List<Double> b = List.of(0.0, 1.0);
        assertEquals(0.0, scorer.cosine(a, b), 0.0001);
    }

    @Test
    void testEuclideanDistance() {
        List<Double> a = List.of(0.0, 0.0);
        List<Double> b = List.of(3.0, 4.0);
        assertEquals(0.1666, scorer.euclidean(a, b), 0.001);
    }

    @Test
    void testDotProduct() {
        List<Double> a = List.of(1.0, 2.0, 3.0);
        List<Double> b = List.of(4.0, 5.0, 6.0);
        assertEquals(32.0, scorer.dotProduct(a, b), 0.0001);
    }

    @Test
    void testScoreCosine() {
        List<Double> source = List.of(1.0, 0.0);
        List<Double> target = List.of(0.0, 1.0);
        SemanticSimilarityScore score = scorer.score("COSINE", source, target, "src", "tgt");
        assertEquals(0.0, score.similarity(), 0.0001);
        assertEquals("src", score.sourceId());
        assertEquals("tgt", score.targetId());
    }
}
