package com.sporekart.memory.embedding;

import java.util.UUID;

public record EmbeddingResult(
    UUID embeddingId,
    float[] vector,
    int dimensions,
    long processingTimeMs,
    String model
) {}
