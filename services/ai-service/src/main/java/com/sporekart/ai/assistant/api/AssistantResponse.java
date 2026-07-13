package com.sporekart.ai.assistant.api;

import com.sporekart.ai.assistant.domain.AssistantTask;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AssistantResponse(
    UUID sessionId,
    String message,
    String intent,
    double confidence,
    List<AssistantTask> tasks,
    Map<String, Object> context,
    boolean requiresFollowUp,
    OffsetDateTime timestamp) {}
