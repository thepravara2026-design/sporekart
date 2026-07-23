package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;

public record RiskAlert(
    String riskId,
    String riskType,
    String severity,
    String title,
    String description,
    double probability,
    double impact,
    String affectedArea,
    String recommendedAction,
    String status,
    OffsetDateTime detectedAt
) {}
