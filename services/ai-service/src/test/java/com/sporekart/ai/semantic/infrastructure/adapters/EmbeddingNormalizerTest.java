package com.sporekart.ai.semantic.infrastructure.adapters;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmbeddingNormalizerTest {

    @Test
    void testL2Normalize() {
        List<Double> vector = List.of(3.0, 4.0);
        List<Double> normalized = EmbeddingNormalizer.l2Normalize(vector);
        assertEquals(0.6, normalized.get(0), 1e-10);
        assertEquals(0.8, normalized.get(1), 1e-10);
    }

    @Test
    void testL2NormalizeZeroVector() {
        List<Double> vector = List.of(0.0, 0.0, 0.0);
        List<Double> normalized = EmbeddingNormalizer.l2Normalize(vector);
        assertEquals(vector, normalized);
    }

    @Test
    void testL2NormalizeNull() {
        assertNull(EmbeddingNormalizer.l2Normalize(null));
    }

    @Test
    void testMinMaxScale() {
        List<Double> vector = List.of(1.0, 3.0, 5.0);
        List<Double> scaled = EmbeddingNormalizer.minMaxScale(vector);
        assertEquals(0.0, scaled.get(0), 1e-10);
        assertEquals(0.5, scaled.get(1), 1e-10);
        assertEquals(1.0, scaled.get(2), 1e-10);
    }

    @Test
    void testCosineSimilarity() {
        List<Double> a = List.of(1.0, 0.0);
        List<Double> b = List.of(0.0, 1.0);
        assertEquals(0.0, EmbeddingNormalizer.cosineSimilarity(a, b), 1e-10);
    }

    @Test
    void testCosineSimilarityIdentical() {
        List<Double> a = List.of(1.0, 2.0, 3.0);
        assertEquals(1.0, EmbeddingNormalizer.cosineSimilarity(a, a), 1e-10);
    }

    @Test
    void testEuclideanDistance() {
        List<Double> a = List.of(0.0, 0.0);
        List<Double> b = List.of(3.0, 4.0);
        assertEquals(5.0, EmbeddingNormalizer.euclideanDistance(a, b), 1e-10);
    }

    @Test
    void testDotProduct() {
        List<Double> a = List.of(1.0, 2.0, 3.0);
        List<Double> b = List.of(4.0, 5.0, 6.0);
        assertEquals(32.0, EmbeddingNormalizer.dotProduct(a, b), 1e-10);
    }
}
