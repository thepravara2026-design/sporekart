package com.sporekart.ai.runtime.infrastructure.executor;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AgentTaskExecutor {

    public CompletableFuture<String> executeAsync(String agentId, String input) {
        return CompletableFuture.completedFuture(null);
    }

    public String executeSync(String agentId, String input) {
        return null;
    }

    public void cancel(String executionId) {
    }

    public boolean isRunning(String executionId) {
        return false;
    }

    public int getActiveTaskCount() {
        return 0;
    }
}
