package com.sporekart.executive.copilot.domain;

import java.util.Map;

public record CompanyHealth(
    double overallScore,
    double previousScore,
    double trend,
    Map<String, Double> dimensions,
    String riskLevel,
    String summary
) {}
