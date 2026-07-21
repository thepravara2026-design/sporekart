package com.sporekart.ai.gateway.domain;

import java.time.Duration;
import java.time.Instant;

public record GatewayMetrics(
    String pipelineId,
    String userId,
    String tenantId,
    String providerId,
    String model,
    Duration totalDuration,
    Duration pipelineDuration,
    Duration providerDuration,
    int inputTokens,
    int outputTokens,
    boolean success,
    String errorCode,
    Instant timestamp
) {}
