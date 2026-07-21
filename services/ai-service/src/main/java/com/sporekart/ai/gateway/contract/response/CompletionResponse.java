package com.sporekart.ai.gateway.contract.response;

import java.util.List;

public record CompletionResponse(
    String id,
    String model,
    List<Choice> choices,
    Usage usage,
    String fingerprint
) {
    public record Choice(int index, String text, String finishReason) {}
    public record Usage(int promptTokens, int completionTokens, int totalTokens) {}
}
