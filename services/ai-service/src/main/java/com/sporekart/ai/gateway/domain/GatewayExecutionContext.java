package com.sporekart.ai.gateway.domain;

import com.sporekart.ai.core.domain.CorrelationId;

import java.time.OffsetDateTime;
import java.util.Map;

public record GatewayExecutionContext(
    String executionId,
    CorrelationId correlationId,
    String userId,
    String module,
    String provider,
    OffsetDateTime timestamp,
    Map<String, Object> metadata
) {
    public String executionId() { return executionId; }
    public CorrelationId correlationId() { return correlationId; }
    public String userId() { return userId; }
    public String module() { return module; }
    public String provider() { return provider; }
    public OffsetDateTime timestamp() { return timestamp; }
    public Map<String, Object> metadata() { return metadata; }

    public static GatewayExecutionContext create(String userId, String tenantId, java.util.List<String> roles,
            String providerId, String model, String deploymentId,
            java.util.Map<String, String> headers, java.util.Map<String, Object> metadata) {
        return new GatewayExecutionContext(
            java.util.UUID.randomUUID().toString(),
            new CorrelationId(java.util.UUID.randomUUID().toString()),
            userId,
            "ai-gateway",
            providerId,
            OffsetDateTime.now(),
            metadata != null ? metadata : java.util.Map.of()
        );
    }
}
