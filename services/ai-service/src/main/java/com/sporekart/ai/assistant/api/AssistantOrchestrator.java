package com.sporekart.ai.assistant.api;

import com.sporekart.ai.assistant.domain.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AssistantOrchestrator {
    AssistantResponse chat(UUID sessionId, String message);
    IntentResult resolveIntent(String message);
    TaskPlan createTaskPlan(AssistantIntent intent);
    Map<String, Object> getStatistics();
    List<AssistantExecution> getExecutionHistory(UUID sessionId);
}
