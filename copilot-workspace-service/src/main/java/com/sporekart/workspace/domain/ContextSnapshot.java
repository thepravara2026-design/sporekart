package com.sporekart.workspace.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record ContextSnapshot(
    String snapshotId,
    String sessionId,
    String userId,
    Map<String, Object> userContext,
    Map<String, Object> conversationContext,
    Map<String, Object> businessContext,
    Map<String, Object> knowledgeContext,
    Map<String, Object> memoryContext,
    OffsetDateTime timestamp
) {}
