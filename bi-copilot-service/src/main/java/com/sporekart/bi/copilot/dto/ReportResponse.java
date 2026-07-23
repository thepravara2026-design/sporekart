package com.sporekart.bi.copilot.dto;

import java.time.OffsetDateTime;

public record ReportResponse(
    String reportId,
    String title,
    String format,
    String status,
    String downloadUrl,
    int pageCount,
    OffsetDateTime generatedAt
) {}