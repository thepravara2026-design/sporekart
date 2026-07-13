package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.UUID;

public record RiskThreshold(
    UUID id,
    RiskLevel level,
    double minScore,
    double maxScore,
    String action
) {}
