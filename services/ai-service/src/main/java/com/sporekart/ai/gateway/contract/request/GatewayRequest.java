package com.sporekart.ai.gateway.contract.request;

import com.sporekart.ai.gateway.contract.message.ChatMessage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record GatewayRequest(
    String requestId,
    String type,
    String provider,
    String model,
    List<ChatMessage> messages,
    String prompt,
    Map<String, Object> parameters,
    Double temperature,
    Integer maxTokens,
    Boolean stream,
    String userId,
    String tenantId,
    Map<String, String> metadata
) {
    public Optional<List<ChatMessage>> getMessages() { return Optional.ofNullable(messages); }
    public Optional<String> getPrompt() { return Optional.ofNullable(prompt); }
    public Optional<Double> getTemperature() { return Optional.ofNullable(temperature); }
    public Optional<Integer> getMaxTokens() { return Optional.ofNullable(maxTokens); }
    public Optional<Boolean> getStream() { return Optional.ofNullable(stream); }
    public Optional<String> getUserId() { return Optional.ofNullable(userId); }
    public Optional<String> getTenantId() { return Optional.ofNullable(tenantId); }
}
