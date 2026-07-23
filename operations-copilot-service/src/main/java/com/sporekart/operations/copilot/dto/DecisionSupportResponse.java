package com.sporekart.operations.copilot.dto;

import java.util.List;

public record DecisionSupportResponse(
    String recommendation,
    double projectedImpact,
    double estimatedCost,
    double confidenceScore,
    String riskLevel,
    List<String> supportingData,
    List<String> alternatives
) {}
