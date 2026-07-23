package com.sporekart.operations.copilot.domain;

import java.util.List;
import java.util.Map;

public record ForecastingResult(
    String productId,
    String productName,
    int currentStock,
    int predictedDemandNext30Days,
    int predictedDemandNext90Days,
    int suggestedReorderQuantity,
    int suggestedSafetyStock,
    double confidenceScore,
    String seasonalityPattern,
    Map<String, Integer> weeklyBreakdown,
    List<String> recommendations
) {}
