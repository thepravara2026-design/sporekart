package com.sporekart.executive.copilot.domain;

public record FinancialMetric(
    String metricName,
    double currentValue,
    double previousValue,
    double targetValue,
    String unit,
    String trend,
    double variancePct
) {}
