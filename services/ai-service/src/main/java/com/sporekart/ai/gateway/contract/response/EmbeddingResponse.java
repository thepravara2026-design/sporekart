package com.sporekart.ai.gateway.contract.response;

import java.util.List;

public record EmbeddingResponse(
    String id,
    String model,
    List<Embedding> embeddings,
    Usage usage
) {
    public record Embedding(int index, List<Double> vector) {}
    public record Usage(int promptTokens, int totalTokens) {}
}
