package com.sporekart.ai.gateway.contract.response;

import com.sporekart.ai.gateway.contract.message.ChatMessage;

import java.util.List;

public record ChatCompletionResponse(
    String id,
    String model,
    List<ChatChoice> choices,
    Usage usage,
    String fingerprint
) {
    public record ChatChoice(int index, ChatMessage message, String finishReason) {}
    public record Usage(int promptTokens, int completionTokens, int totalTokens) {}
}
