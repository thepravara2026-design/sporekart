package com.sporekart.memory.embedding;

import java.util.UUID;

public record SimilarityResult(
    UUID memoryId,
    UUID embeddingId,
    double score,
    String content
) {}
