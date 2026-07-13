package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.UUID;

public record RiskHistory(
    UUID id,
    UUID assessmentId,
    String eventType,
    String description,
    Instant timestamp
) {}
