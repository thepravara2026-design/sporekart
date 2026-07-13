package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record GovernanceReport(
    UUID id,
    ReportType type,
    String title,
    String description,
    Map<String, Object> data,
    Map<String, Object> summary,
    Instant generatedAt,
    UUID generatedBy
) {}
