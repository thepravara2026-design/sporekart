package com.sporekart.ai.assistant.interfaces.rest.dto;

import com.sporekart.ai.assistant.domain.AssistantMetrics;
import java.time.OffsetDateTime;

public record StatisticsResponse(
        long totalRequests,
        long successfulRequests,
        double avgLatencyMs,
        double intentAccuracy,
        long tasksCompleted,
        OffsetDateTime recordedAt
) {
    public static StatisticsResponse from(AssistantMetrics metrics) {
        return new StatisticsResponse(
                metrics.totalRequests(),
                metrics.successfulRequests(),
                metrics.avgLatencyMs(),
                metrics.intentAccuracy(),
                metrics.tasksCompleted(),
                metrics.recordedAt()
        );
    }
}
