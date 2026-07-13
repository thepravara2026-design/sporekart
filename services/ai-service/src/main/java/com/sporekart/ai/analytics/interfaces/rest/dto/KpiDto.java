package com.sporekart.ai.analytics.interfaces.rest.dto;

public record KpiDto(
    String id,
    String name,
    String description,
    String module,
    double currentValue,
    double targetValue,
    String status,
    String calculatedAt
) {}
