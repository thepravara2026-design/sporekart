package com.sporekart.ai.runtime.api;

import com.sporekart.ai.runtime.domain.AgentDefinition;
import com.sporekart.ai.runtime.domain.AgentExecution;

import java.util.List;

public interface AgentOrchestrator {
    AgentExecution executeWithTools(AgentDefinition definition, String input, String sessionId, String userId);
    AgentExecution executeWithMemory(AgentDefinition definition, String input, String sessionId, String userId);
    AgentExecution executeWithWorkflow(AgentDefinition definition, String input, String sessionId, String userId, List<String> workflowSteps);
    void stopAgent(String agentId);
}
