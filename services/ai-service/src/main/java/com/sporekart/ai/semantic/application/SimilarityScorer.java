package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSimilarityScore;
import com.sporekart.ai.semantic.infrastructure.adapters.EmbeddingNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimilarityScorer {

    private static final Logger log = LoggerFactory.getLogger(SimilarityScorer.class);

    public double cosine(List<Double> a, List<Double> b) {
        return EmbeddingNormalizer.cosineSimilarity(a, b);
    }

    public double euclidean(List<Double> a, List<Double> b) {
        return 1.0 / (1.0 + EmbeddingNormalizer.euclideanDistance(a, b));
    }

    public double dotProduct(List<Double> a, List<Double> b) {
        return EmbeddingNormalizer.dotProduct(a, b);
    }

    public SemanticSimilarityScore score(String algorithm, List<Double> source, List<Double> target,
                                          String sourceId, String targetId) {
        double similarity = switch (algorithm.toUpperCase()) {
            case "COSINE" -> cosine(source, target);
            case "DOT_PRODUCT" -> dotProduct(source, target);
            case "EUCLIDEAN" -> euclidean(source, target);
            default -> cosine(source, target);
        };
        log.debug("Similarity score: {} vs {} = {} (algorithm={})", sourceId, targetId, similarity, algorithm);
        return new SemanticSimilarityScore(sourceId, targetId, similarity, algorithm);
    }
}
