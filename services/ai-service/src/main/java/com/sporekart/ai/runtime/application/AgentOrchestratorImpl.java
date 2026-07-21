package com.sporekart.ai.runtime.application;

import com.sporekart.ai.runtime.api.AgentOrchestrator;
import com.sporekart.ai.runtime.domain.AgentDefinition;
import com.sporekart.ai.runtime.domain.AgentExecution;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentOrchestratorImpl implements AgentOrchestrator {

    @Override
    public AgentExecution executeWithTools(AgentDefinition definition, String input, String sessionId, String userId) {
        return null;
    }

    @Override
    public AgentExecution executeWithMemory(AgentDefinition definition, String input, String sessionId, String userId) {
        return null;
    }

    @Override
    public AgentExecution executeWithWorkflow(AgentDefinition definition, String input, String sessionId, String userId, List<String> workflowSteps) {
        return null;
    }

    @Override
    public void stopAgent(String agentId) {
    }
}
