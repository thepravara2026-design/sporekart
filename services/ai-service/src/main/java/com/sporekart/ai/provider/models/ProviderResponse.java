package com.sporekart.ai.provider.models;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public record ProviderResponse(
        String content,
        String model,
        String providerName,
        TokenUsage tokenUsage,
        Duration latency,
        boolean success,
        Optional<String> errorMessage,
        Map<String, String> metadata) {

    public ProviderResponse {
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        errorMessage = errorMessage == null ? Optional.empty() : errorMessage;
    }

    public static ProviderResponse success(String content, String model, String providerName, TokenUsage tokenUsage, Duration latency) {
        return new ProviderResponse(content, model, providerName, tokenUsage, latency, true, Optional.empty(), Map.of());
    }

    public static ProviderResponse failure(String errorMessage, String providerName) {
        return new ProviderResponse(null, null, providerName, null, null, false, Optional.of(errorMessage), Map.of());
    }

    public record TokenUsage(int inputTokens, int outputTokens, int totalTokens) {
        public static TokenUsage of(int input, int output) {
            return new TokenUsage(input, output, input + output);
        }
    }
}
