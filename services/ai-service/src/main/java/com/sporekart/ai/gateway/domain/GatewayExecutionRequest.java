package com.sporekart.ai.gateway.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record GatewayExecutionRequest(
    String executionId,
    String userId,
    String tenantId,
    List<String> roles,
    String providerId,
    String model,
    String deploymentId,
    Map<String, String> headers,
    Map<String, Object> metadata,
    Instant startedAt
) {
    public GatewayExecutionRequest {
        executionId = executionId != null ? executionId : UUID.randomUUID().toString();
        startedAt = startedAt != null ? startedAt : Instant.now();
    }

    public GatewayExecutionRequest(String userId, String tenantId, List<String> roles,
            String providerId, String model, String deploymentId,
            Map<String, String> headers, Map<String, Object> metadata) {
        this(UUID.randomUUID().toString(), userId, tenantId, roles, providerId, model,
            deploymentId, headers, metadata, Instant.now());
    }

    public Optional<String> getHeader(String name) {
        return headers != null ? Optional.ofNullable(headers.get(name)) : Optional.empty();
    }

    public Optional<Object> getMetadata(String key) {
        return metadata != null ? Optional.ofNullable(metadata.get(key)) : Optional.empty();
    }

    public GatewayExecutionContext toExecutionContext() {
        return GatewayExecutionContext.create(userId, tenantId, roles, providerId, model, deploymentId, headers, metadata);
    }
}
