package com.sporekart.operations.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record ForecastRequest(
    @NotBlank String productId,
    String region,
    Integer forecastDays
) {}
