package com.sporekart.memory.embedding;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmbeddingProvider {
    String providerName();
    EmbeddingResult generateEmbedding(String text);
    List<EmbeddingResult> generateEmbeddings(List<String> texts);
    boolean deleteEmbedding(UUID embeddingId);
    Optional<EmbeddingResult> updateEmbedding(UUID embeddingId, String text);
    List<SimilarityResult> searchSimilar(float[] vector, int maxResults, double minScore);
    int dimensions();
    boolean isAvailable();
}
