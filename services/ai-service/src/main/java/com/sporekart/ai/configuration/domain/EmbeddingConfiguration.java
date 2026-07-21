package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record EmbeddingConfiguration(
    boolean enabled,
    String defaultProvider,
    String defaultModel,
    int embeddingDimension,
    int batchSize,
    Duration cacheTtl,
    boolean normalizeEmbeddings,
    boolean cachingEnabled,
    boolean fallbackEnabled,
    String fallbackProvider,
    String fallbackModel
) {
    public static EmbeddingConfiguration defaults() {
        return new EmbeddingConfiguration(
            true, "openai", "text-embedding-3-small", 1536,
            20, Duration.ofHours(24),
            true, true, true,
            "gemini", "embedding-001"
        );
    }
}
