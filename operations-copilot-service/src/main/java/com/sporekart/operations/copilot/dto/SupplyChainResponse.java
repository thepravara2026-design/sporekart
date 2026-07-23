package com.sporekart.operations.copilot.dto;

import java.util.List;

public record SupplyChainResponse(
    String summary,
    double healthScore,
    List<RiskItem> risks,
    List<String> recommendations
) {
    public record RiskItem(String title, String category, String severity, double impact, String affectedEntity) {}
}
