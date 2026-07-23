package com.sporekart.executive.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record ExecutiveQueryRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String period,
    String department,
    String forecastType
) {}
