package com.sporekart.executive.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record ForecastRequest(
    @NotBlank String forecastType,
    String period,
    Integer horizonMonths
) {}
