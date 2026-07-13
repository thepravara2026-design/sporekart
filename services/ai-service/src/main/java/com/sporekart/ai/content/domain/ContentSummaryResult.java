package com.sporekart.ai.content.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentSummaryResult(
    UUID id,
    UUID requestId,
    String summary,
    int originalLength,
    int summaryLength,
    double compressionRatio,
    String language,
    OffsetDateTime generatedAt) {}
