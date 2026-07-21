package com.sporekart.ai.runtime.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.modules.agent-runtime")
public class AgentRuntimeConfig {

    private boolean enabled = true;
    private int maxAgents = 100;
    private Duration executionTimeout = Duration.ofMinutes(5);
    private int maxIterations = 25;
    private Execution execution = new Execution();
    private Scheduling scheduling = new Scheduling();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxAgents() {
        return maxAgents;
    }

    public void setMaxAgents(int maxAgents) {
        this.maxAgents = maxAgents;
    }

    public Duration getExecutionTimeout() {
        return executionTimeout;
    }

    public void setExecutionTimeout(Duration executionTimeout) {
        this.executionTimeout = executionTimeout;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public Scheduling getScheduling() {
        return scheduling;
    }

    public void setScheduling(Scheduling scheduling) {
        this.scheduling = scheduling;
    }

    public static class Execution {
        private boolean asyncEnabled = true;
        private int maxConcurrentExecutions = 50;
        private boolean retryEnabled = true;
        private int maxRetries = 3;

        public boolean isAsyncEnabled() {
            return asyncEnabled;
        }

        public void setAsyncEnabled(boolean asyncEnabled) {
            this.asyncEnabled = asyncEnabled;
        }

        public int getMaxConcurrentExecutions() {
            return maxConcurrentExecutions;
        }

        public void setMaxConcurrentExecutions(int maxConcurrentExecutions) {
            this.maxConcurrentExecutions = maxConcurrentExecutions;
        }

        public boolean isRetryEnabled() {
            return retryEnabled;
        }

        public void setRetryEnabled(boolean retryEnabled) {
            this.retryEnabled = retryEnabled;
        }

        public int getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }
    }

    public static class Scheduling {
        private boolean enabled = true;
        private String threadPoolSize = "4";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getThreadPoolSize() {
            return threadPoolSize;
        }

        public void setThreadPoolSize(String threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
        }
    }
}
