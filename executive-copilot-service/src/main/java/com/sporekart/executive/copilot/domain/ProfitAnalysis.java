package com.sporekart.executive.copilot.domain;

public record ProfitAnalysis(
    double grossProfit,
    double grossMargin,
    double netProfit,
    double netMargin,
    double operatingExpenses,
    double ebitda,
    double ebitdaMargin
) {}
