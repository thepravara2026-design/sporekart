package com.sporekart.ai.providers.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ProviderResponse(
    String responseId,
    String requestId,
    String providerId,
    String model,
    boolean success,
    List<String> generatedText,
    Map<String, Object> data,
    String errorCode,
    String errorMessage,
    int statusCode,
    TokenUsage tokenUsage,
    Map<String, Object> metadata,
    Instant timestamp
) {
    public record TokenUsage(int promptTokens, int completionTokens, int totalTokens) {}

    public static ProviderResponse ok(String requestId, String providerId, String model, List<String> text) {
        return new ProviderResponse(java.util.UUID.randomUUID().toString(), requestId, providerId, model,
            true, text, null, null, null, 200, null, null, Instant.now());
    }

    public static ProviderResponse error(String requestId, String providerId, int code, String errorCode, String message) {
        return new ProviderResponse(java.util.UUID.randomUUID().toString(), requestId, providerId, null,
            false, null, null, errorCode, message, code, null, null, Instant.now());
    }

    public Optional<String> getErrorCode() { return Optional.ofNullable(errorCode); }
    public Optional<String> getErrorMessage() { return Optional.ofNullable(errorMessage); }
}
