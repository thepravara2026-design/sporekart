package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record GovernanceExport(
    UUID id,
    UUID reportId,
    ReportFormat format,
    String fileName,
    long fileSize,
    Map<String, Object> metadata,
    Instant exportedAt,
    UUID exportedBy
) {}
