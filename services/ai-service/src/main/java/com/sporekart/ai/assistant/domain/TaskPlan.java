package com.sporekart.ai.assistant.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record TaskPlan(
    UUID id,
    List<AssistantTask> tasks,
    Map<String, List<String>> dependencies,
    int totalEstimatedDurationMs,
    boolean hasParallelExecution
) {}
