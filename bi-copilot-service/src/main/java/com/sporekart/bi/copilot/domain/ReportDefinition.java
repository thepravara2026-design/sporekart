package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record ReportDefinition(
    String reportId,
    String name,
    String type,
    String format,
    String schedule,
    List<String> metrics,
    List<String> dimensions,
    Map<String, Object> filters,
    List<String> recipients,
    String status,
    OffsetDateTime lastGeneratedAt
) {}
