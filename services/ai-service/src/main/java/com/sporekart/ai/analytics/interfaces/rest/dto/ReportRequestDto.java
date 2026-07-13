package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.Map;

public record ReportRequestDto(
    String type,
    String title,
    String description,
    Map<String, Object> params
) {}
