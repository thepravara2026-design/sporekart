package com.sporekart.copilot.dto;

public record HealthResponse(
    String status,
    String version,
    long uptime,
    int copilotCount
) {}
