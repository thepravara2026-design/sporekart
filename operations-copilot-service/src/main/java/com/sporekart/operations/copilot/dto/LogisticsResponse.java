package com.sporekart.operations.copilot.dto;

import java.util.List;

public record LogisticsResponse(
    String recommendation,
    List<CourierOption> courierOptions,
    double estimatedCost,
    int estimatedDays,
    List<String> alerts
) {
    public record CourierOption(String name, double price, int estimatedDays, double reliability, String recommendation, String riskLevel) {}
}
