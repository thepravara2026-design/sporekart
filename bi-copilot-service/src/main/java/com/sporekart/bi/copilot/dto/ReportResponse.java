package com.sporekart.bi.copilot.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record ReportResponse(
    String reportId,
    String name,
    String status,
    String format,
    String downloadUrl,
    Map<String, Object> summary,
    OffsetDateTime generatedAt
) {}
