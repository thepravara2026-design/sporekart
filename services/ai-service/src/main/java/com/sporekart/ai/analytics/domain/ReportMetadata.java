package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.UUID;

public record ReportMetadata(
    UUID id,
    UUID reportId,
    String key,
    String value,
    String category
) {}
