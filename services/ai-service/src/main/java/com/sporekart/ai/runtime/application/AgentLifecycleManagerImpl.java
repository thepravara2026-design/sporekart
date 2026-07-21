package com.sporekart.ai.runtime.application;

import com.sporekart.ai.runtime.api.AgentLifecycleManager;
import com.sporekart.ai.runtime.domain.AgentStatus;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AgentLifecycleManagerImpl implements AgentLifecycleManager {

    private final Map<UUID, AgentStatus> agentStatuses = new ConcurrentHashMap<>();

    @Override
    public void activate(UUID agentId) {
        agentStatuses.put(agentId, AgentStatus.ACTIVE);
    }

    @Override
    public void deactivate(UUID agentId) {
        agentStatuses.put(agentId, AgentStatus.DISABLED);
    }

    @Override
    public void pause(UUID agentId) {
        agentStatuses.put(agentId, AgentStatus.PAUSED);
    }

    @Override
    public void resume(UUID agentId) {
        agentStatuses.put(agentId, AgentStatus.ACTIVE);
    }

    @Override
    public void archive(UUID agentId) {
        agentStatuses.put(agentId, AgentStatus.ARCHIVED);
    }

    @Override
    public AgentStatus getStatus(UUID agentId) {
        return agentStatuses.getOrDefault(agentId, AgentStatus.DRAFT);
    }

    @Override
    public boolean isActive(UUID agentId) {
        return agentStatuses.getOrDefault(agentId, AgentStatus.DRAFT) == AgentStatus.ACTIVE;
    }
}
