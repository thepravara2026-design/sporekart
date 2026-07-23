package com.sporekart.operations.copilot.dto;

import java.util.List;
import java.util.Map;

public record ForecastResponse(
    String productId,
    String productName,
    int currentStock,
    int predictedDemand,
    int suggestedOrder,
    double confidence,
    Map<String, Integer> weeklyDemand,
    List<String> recommendations
) {}
