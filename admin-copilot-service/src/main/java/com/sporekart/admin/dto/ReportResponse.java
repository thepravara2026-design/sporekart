package com.sporekart.admin.dto;

import java.time.OffsetDateTime;

public record ReportResponse(
    String reportId,
    String title,
    String downloadUrl,
    OffsetDateTime generatedAt,
    String format,
    long size
) {}
