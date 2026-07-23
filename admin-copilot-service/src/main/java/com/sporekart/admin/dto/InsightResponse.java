package com.sporekart.admin.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record InsightResponse(
    String summary,
    Map<String, Object> keyMetrics,
    List<Map<String, Object>> trends,
    List<String> recommendations,
    OffsetDateTime generatedAt
) {}
