package com.sporekart.ai.semantic.domain;

public record SemanticSimilarityScore(
        String sourceId,
        String targetId,
        double similarity,
        String algorithm) {
}
