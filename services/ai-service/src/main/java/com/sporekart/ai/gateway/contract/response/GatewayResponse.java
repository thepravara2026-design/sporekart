package com.sporekart.ai.gateway.contract.response;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record GatewayResponse(
    String responseId,
    String requestId,
    boolean success,
    String provider,
    String model,
    List<String> generatedText,
    Map<String, Object> data,
    String errorCode,
    String errorMessage,
    int statusCode,
    Map<String, Object> metadata,
    Instant timestamp
) {
    public static GatewayResponse ok(String requestId, String provider, String model, List<String> generatedText) {
        return new GatewayResponse(java.util.UUID.randomUUID().toString(), requestId, true,
            provider, model, generatedText, null, null, null, 200, null, Instant.now());
    }

    public static GatewayResponse error(String requestId, int statusCode, String errorCode, String message) {
        return new GatewayResponse(java.util.UUID.randomUUID().toString(), requestId, false,
            null, null, null, null, errorCode, message, statusCode, null, Instant.now());
    }

    public Optional<String> getErrorCode() { return Optional.ofNullable(errorCode); }
    public Optional<String> getErrorMessage() { return Optional.ofNullable(errorMessage); }
    public Optional<List<String>> getGeneratedText() { return Optional.ofNullable(generatedText); }
}
