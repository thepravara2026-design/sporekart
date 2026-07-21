package com.sporekart.ai.gateway.contract.response;

public record VisionResponse(
    String id,
    String model,
    String description,
    Usage usage
) {
    public record Usage(int promptTokens, int completionTokens, int totalTokens) {}
}
