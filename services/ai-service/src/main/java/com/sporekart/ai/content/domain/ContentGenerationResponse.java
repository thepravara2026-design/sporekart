package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentGenerationResponse(
    UUID id,
    String requestId,
    String content,
    ContentType contentType,
    ContentCategory category,
    ContentTone tone,
    int tokenCount,
    double confidenceScore,
    boolean humanReviewRequired,
    ModerationStatus moderationStatus,
    OffsetDateTime generatedAt,
    long latencyMs,
    boolean success,
    String errorMessage) {}
