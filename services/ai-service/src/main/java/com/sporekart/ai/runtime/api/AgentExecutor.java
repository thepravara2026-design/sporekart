package com.sporekart.ai.runtime.api;

import com.sporekart.ai.runtime.domain.AgentDefinition;
import com.sporekart.ai.runtime.domain.AgentExecution;

import java.util.UUID;

public interface AgentExecutor {
    AgentExecution execute(AgentDefinition definition, String input, String sessionId, String userId);
    AgentExecution executeSync(AgentDefinition definition, String input, String sessionId, String userId);
    void cancel(UUID executionId);
    boolean isRunning(UUID executionId);
}
