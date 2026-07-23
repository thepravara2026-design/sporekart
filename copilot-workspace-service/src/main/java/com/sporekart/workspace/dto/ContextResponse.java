package com.sporekart.workspace.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record ContextResponse(
    String sessionId,
    Map<String, Object> userContext,
    Map<String, Object> conversationContext,
    Map<String, Object> businessContext,
    Map<String, Object> knowledgeContext,
    OffsetDateTime timestamp
) {}
