package com.sporekart.ai.semantic.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record SemanticEmbedding(
        String id,
        String content,
        List<Double> vector,
        String provider,
        String model,
        int dimensions,
        Map<String, String> metadata,
        EmbeddingStatus status,
        int version,
        OffsetDateTime createdAt) {
}
