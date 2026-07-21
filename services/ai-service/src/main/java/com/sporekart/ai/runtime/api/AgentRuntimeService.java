package com.sporekart.ai.runtime.api;

import com.sporekart.ai.runtime.domain.AgentDefinition;
import com.sporekart.ai.runtime.domain.AgentExecution;
import com.sporekart.ai.runtime.domain.AgentMetrics;
import com.sporekart.ai.runtime.domain.AgentStatus;
import com.sporekart.ai.runtime.domain.AgentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgentRuntimeService {
    AgentDefinition register(AgentDefinition definition);
    Optional<AgentDefinition> get(UUID id);
    List<AgentDefinition> list(AgentType type, AgentStatus status);
    AgentDefinition update(AgentDefinition definition);
    void delete(UUID id);
    AgentExecution execute(UUID agentId, String input, String sessionId, String userId);
    AgentExecution executeSync(UUID agentId, String input, String sessionId, String userId);
    Optional<AgentExecution> getExecution(UUID executionId);
    List<AgentExecution> getExecutionHistory(UUID agentId, int limit);
    void cancelExecution(UUID executionId);
    AgentMetrics getMetrics();
    void setStatus(UUID agentId, AgentStatus status);
    boolean healthCheck();
}
