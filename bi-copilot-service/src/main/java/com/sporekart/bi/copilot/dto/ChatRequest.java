package com.sporekart.bi.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
    @NotBlank String message,
    String sessionId,
    String pageUrl,
    String pageTitle,
    String section,
    String entityType,
    String entityId,
    String dashboardId,
    String reportType
) {}
