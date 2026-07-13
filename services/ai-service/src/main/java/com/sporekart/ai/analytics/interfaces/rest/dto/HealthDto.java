package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.Map;

public record HealthDto(
    String status,
    String service,
    long timestamp,
    Map<String, Object> details
) {}
