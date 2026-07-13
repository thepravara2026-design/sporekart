package com.sporekart.ai.risk.interfaces.rest.dto;

public record RiskAssessResponse(
    String assessmentId,
    boolean proceed,
    String riskLevel,
    double riskScore,
    double trustScore,
    double confidenceScore,
    String recommendation,
    String message,
    long timestamp
) {}
