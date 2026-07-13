package com.sporekart.ai.content.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ContentModerationResult(
    UUID id,
    UUID requestId,
    ModerationStatus status,
    boolean containsPii,
    boolean containsProfanity,
    boolean isToxic,
    double confidenceScore,
    List<String> flags,
    boolean humanReviewRequired,
    UUID reviewedBy,
    OffsetDateTime reviewedAt) {}
