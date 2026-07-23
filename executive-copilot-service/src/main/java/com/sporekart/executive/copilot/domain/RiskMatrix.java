package com.sporekart.executive.copilot.domain;

import java.util.List;

public record RiskMatrix(
    List<BusinessRisk> criticalRisks,
    List<BusinessRisk> highRisks,
    List<BusinessRisk> mediumRisks,
    List<BusinessRisk> lowRisks,
    double overallRiskScore,
    String riskLevel
) {}
