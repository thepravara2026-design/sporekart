package com.sporekart.ai.gateway.domain;

import java.time.OffsetDateTime;

public record CorrelationMetadata(
        String correlationId,
        String requestId,
        String source,
        OffsetDateTime timestamp) {
}
