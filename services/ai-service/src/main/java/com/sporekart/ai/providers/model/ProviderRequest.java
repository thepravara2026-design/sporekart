package com.sporekart.ai.providers.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ProviderRequest(
    String requestId,
    String providerId,
    String model,
    String operation,
    String prompt,
    List<Map<String, Object>> messages,
    Map<String, Object> parameters,
    Double temperature,
    Integer maxTokens,
    Boolean stream,
    String userId,
    String tenantId,
    Map<String, Object> metadata,
    Instant timestamp
) {
    public Optional<String> getPrompt() { return Optional.ofNullable(prompt); }
    public Optional<List<Map<String, Object>>> getMessages() { return Optional.ofNullable(messages); }
    public Optional<Double> getTemperature() { return Optional.ofNullable(temperature); }
    public Optional<Integer> getMaxTokens() { return Optional.ofNullable(maxTokens); }
}
