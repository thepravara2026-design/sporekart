package com.sporekart.operations.copilot.dto;

import java.util.List;
import java.util.Map;

public record KPIResponse(
    List<KPIData> kpis,
    Map<String, Object> summary
) {
    public record KPIData(String name, String category, double current, double target, double previous, String unit, String trend) {}
}
