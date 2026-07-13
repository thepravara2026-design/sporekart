package com.sporekart.ai.admin.interfaces.rest.dto;

import java.util.Map;

public record AdminStatsDto(
    long totalOperations,
    long configChanges,
    long rollbackCount,
    long featureFlagChanges,
    Map<String, Long> operationDistribution
) {}
