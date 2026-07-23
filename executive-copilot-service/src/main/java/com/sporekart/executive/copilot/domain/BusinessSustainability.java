package com.sporekart.executive.copilot.domain;

public record BusinessSustainability(
    double sustainabilityScore,
    double revenueDiversification,
    double customerConcentration,
    double supplierConcentration,
    double marketDependency,
    String riskLevel,
    String recommendation
) {}
