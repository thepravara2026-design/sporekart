package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.Map;

public record HealthDto(
    String status,
    String service,
    long timestamp,
    Map<String, Object> details
) {}
