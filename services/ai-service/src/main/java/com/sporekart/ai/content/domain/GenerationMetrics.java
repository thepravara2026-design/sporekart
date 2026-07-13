package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GenerationMetrics(
    UUID id,
    UUID requestId,
    ContentType contentType,
    ContentCategory category,
    String pipelineStage,
    long stageLatencyMs,
    long totalLatencyMs,
    int tokenCount,
    boolean success,
    String errorMessage,
    OffsetDateTime createdAt) {}
