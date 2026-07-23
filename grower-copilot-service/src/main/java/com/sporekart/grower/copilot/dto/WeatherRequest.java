package com.sporekart.grower.copilot.dto;

import jakarta.validation.constraints.Positive;

public record WeatherRequest(
    String location,
    @Positive int forecastDays
) {}
