package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentHistoryEntry(
    UUID id,
    UUID requestId,
    UUID userId,
    String action,
    ContentType contentType,
    String prompt,
    String resultSummary,
    int tokenCount,
    long latencyMs,
    boolean success,
    OffsetDateTime createdAt) {}
