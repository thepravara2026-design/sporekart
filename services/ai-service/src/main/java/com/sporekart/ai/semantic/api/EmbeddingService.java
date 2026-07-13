package com.sporekart.ai.semantic.api;

import java.util.List;
import java.util.Map;

public interface EmbeddingService {
    List<Double> generateEmbedding(String content);
    List<List<Double>> generateEmbeddings(List<String> contents);
    boolean validateEmbedding(List<Double> embedding);
    EmbeddingMetadata getEmbeddingMetadata(String id);

    record EmbeddingMetadata(
            String id,
            String content,
            String provider,
            String model,
            int dimensions,
            String status,
            int version,
            Map<String, String> metadata,
            String createdAt) {}
}
