package com.sporekart.ai.semantic.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record SemanticDocument(
        String id,
        String content,
        List<Double> embedding,
        String provider,
        String model,
        int dimensions,
        Map<String, String> metadata,
        EmbeddingStatus status,
        int version,
        String createdBy,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public SemanticDocument(String content, String provider, String model) {
        this(null, content, null, provider, model, 0, Map.of(),
                EmbeddingStatus.PENDING, 1, null, OffsetDateTime.now(), OffsetDateTime.now());
    }
}
