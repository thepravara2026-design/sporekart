package com.sporekart.executive.copilot.dto;

import java.util.List;

public record RiskResponse(
    double overallRiskScore,
    String riskLevel,
    List<RiskItem> criticalRisks,
    List<RiskItem> highRisks,
    List<RiskItem> mediumRisks,
    List<RiskItem> lowRisks
) {
    public record RiskItem(String title, String category, double severity, double probability, double score, String impact, List<String> mitigation) {}
}
