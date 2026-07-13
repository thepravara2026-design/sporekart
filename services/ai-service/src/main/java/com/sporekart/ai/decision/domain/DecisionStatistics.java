package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DecisionStatistics(
    UUID id, long totalDecisions, long allowedCount, long deniedCount,
    long escalatedCount, long approvalCount, long conflictCount,
    double averageConfidence, double averageLatencyMs,
    OffsetDateTime calculatedAt) {}
