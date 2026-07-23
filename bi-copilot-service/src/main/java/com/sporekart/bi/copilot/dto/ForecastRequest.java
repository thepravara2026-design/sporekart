package com.sporekart.bi.copilot.dto;

import java.util.Map;

public record ForecastRequest(
    String metric,
    String method,
    int horizon,
    Map<String, Object> parameters
) {}
