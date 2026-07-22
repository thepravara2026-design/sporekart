package com.sporekart.prompt.dto.response;

import java.math.BigDecimal;

public record MetricsResponse(
        long totalExecutions,
        long successfulExecutions,
        long failedExecutions,
        double averageLatencyMs,
        double averageTokens,
        BigDecimal averageCost,
        double successRate,
        double failureRate
) {}
