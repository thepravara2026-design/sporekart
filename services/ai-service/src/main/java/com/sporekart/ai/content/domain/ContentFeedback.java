package com.sporekart.ai.content.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentFeedback(
    UUID id,
    UUID requestId,
    UUID userId,
    int rating,
    String comment,
    boolean isHelpful,
    OffsetDateTime createdAt) {}
