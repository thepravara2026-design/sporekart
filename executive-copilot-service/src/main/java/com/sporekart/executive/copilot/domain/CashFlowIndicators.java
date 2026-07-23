package com.sporekart.executive.copilot.domain;

public record CashFlowIndicators(
    double operatingCashFlow,
    double investingCashFlow,
    double financingCashFlow,
    double freeCashFlow,
    double cashBurnRate,
    double runwayMonths,
    String status
) {}
