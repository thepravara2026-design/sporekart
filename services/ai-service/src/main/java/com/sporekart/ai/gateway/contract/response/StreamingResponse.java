package com.sporekart.ai.gateway.contract.response;

public record StreamingResponse(
    String id,
    String model,
    String delta,
    int index,
    String finishReason,
    Usage usage
) {
    public record Usage(int promptTokens, int completionTokens, int totalTokens) {}
}
