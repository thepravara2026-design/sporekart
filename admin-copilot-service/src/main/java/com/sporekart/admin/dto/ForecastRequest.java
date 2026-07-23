package com.sporekart.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record ForecastRequest(
    @NotBlank String metric,
    @NotBlank String period,
    @Min(1) int horizon,
    Map<String, String> filters
) {}
