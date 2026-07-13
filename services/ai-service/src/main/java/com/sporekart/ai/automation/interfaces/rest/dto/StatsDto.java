package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.Map;

public record StatsDto(
    long totalWorkflows,
    long totalJobs,
    double jobSuccessRate,
    long retryCount,
    long escalationCount,
    Map<String, Object> detailed
) {}
