package com.sporekart.ai.gateway.contract.response;

public record AudioResponse(
    String id,
    String model,
    String transcript,
    byte[] audioData,
    String format,
    Usage usage
) {
    public record Usage(int promptTokens, int completionTokens, int totalTokens) {}
}
