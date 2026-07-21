package com.sporekart.ai.runtime.infrastructure.kafka;

import org.springframework.stereotype.Component;

@Component
public class AgentRuntimeKafkaEventPublisher {

    public void publishAgentRegistered(String agentId) {
    }

    public void publishAgentUpdated(String agentId) {
    }

    public void publishAgentDeleted(String agentId) {
    }

    public void publishExecutionStarted(String executionId) {
    }

    public void publishExecutionCompleted(String executionId) {
    }

    public void publishExecutionFailed(String executionId, String error) {
    }

    public void publishToolInvoked(String agentId, String toolName) {
    }

    public void publishScheduleTriggered(String agentId) {
    }
}
