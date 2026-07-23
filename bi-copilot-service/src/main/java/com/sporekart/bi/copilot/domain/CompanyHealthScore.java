package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;
import java.util.List;

public record CompanyHealthScore(
    double overall,
    double revenueScore,
    double customerScore,
    double trainingScore,
    double inventoryScore,
    double operationsScore,
    double growthScore,
    String period,
    String trend,
    List<HealthFactor> factors,
    OffsetDateTime calculatedAt
) {
    public record HealthFactor(String name, double score, String status, String description) {}
}
