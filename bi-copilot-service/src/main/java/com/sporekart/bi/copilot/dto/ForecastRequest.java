package com.sporekart.bi.copilot.dto;

public record ForecastRequest(
    String metric,
    String period,
    int horizon,
    String method
) {}