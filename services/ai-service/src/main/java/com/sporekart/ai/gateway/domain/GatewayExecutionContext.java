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
        OffsetDateTime startTime,
        Map<String, Object> metadata) {
}
