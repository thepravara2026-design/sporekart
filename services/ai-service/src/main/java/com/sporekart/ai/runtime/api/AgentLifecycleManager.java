package com.sporekart.ai.runtime.api;

import com.sporekart.ai.runtime.domain.AgentStatus;

import java.util.UUID;

public interface AgentLifecycleManager {
    void activate(UUID agentId);
    void deactivate(UUID agentId);
    void pause(UUID agentId);
    void resume(UUID agentId);
    void archive(UUID agentId);
    AgentStatus getStatus(UUID agentId);
    boolean isActive(UUID agentId);
}
