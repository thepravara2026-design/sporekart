package com.sporekart.executive.copilot.dto;

import java.util.List;

public record DecisionResponse(
    String recommendation,
    double expectedROI,
    double expectedImpact,
    double confidenceScore,
    String riskLevel,
    String timeframe,
    List<String> supportingData,
    List<String> alternatives
) {}
