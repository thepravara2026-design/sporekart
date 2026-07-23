package com.sporekart.executive.copilot.domain;

import java.util.List;

public record BusinessRisk(
    String riskId,
    String title,
    String description,
    RiskCategory category,
    double severity,
    double probability,
    double riskScore,
    String impact,
    List<String> mitigationPlan,
    String status
) {
    public enum RiskCategory {
        REVENUE, PROFITABILITY, OPERATIONAL, CUSTOMER_CHURN, INVENTORY,
        MARKETING, CASH_FLOW, SUPPLY_CHAIN, CONCENTRATION, COMPETITIVE, REGULATORY
    }
}
