package com.sporekart.ai.compliance.interfaces.rest.dto;

import java.util.Map;

public record ComplianceStatsDto(
    long totalValidations,
    long passCount,
    long failureCount,
    long violationCount,
    double passRate,
    double avgAssessmentLatencyMs,
    Map<String, Object> detailed
) {}
