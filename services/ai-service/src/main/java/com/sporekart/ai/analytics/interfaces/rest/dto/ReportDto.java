package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.Map;

public record ReportDto(
    String id,
    String type,
    String title,
    String description,
    Map<String, Object> summary,
    String generatedAt
) {}
