package com.sporekart.ai.runtime.application;

import com.sporekart.ai.runtime.api.AgentExecutor;
import com.sporekart.ai.runtime.domain.AgentDefinition;
import com.sporekart.ai.runtime.domain.AgentExecution;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AgentExecutorImpl implements AgentExecutor {

    @Override
    public AgentExecution execute(AgentDefinition definition, String input, String sessionId, String userId) {
        return null;
    }

    @Override
    public AgentExecution executeSync(AgentDefinition definition, String input, String sessionId, String userId) {
        return null;
    }

    @Override
    public void cancel(UUID executionId) {
    }

    @Override
    public boolean isRunning(UUID executionId) {
        return false;
    }
}
