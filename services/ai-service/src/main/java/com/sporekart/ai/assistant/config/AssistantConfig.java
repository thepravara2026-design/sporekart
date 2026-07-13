package com.sporekart.ai.assistant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.assistant")
public class AssistantConfig {

    private boolean enabled = true;
    private SessionConfig session = new SessionConfig();
    private IntentConfig intent = new IntentConfig();
    private TaskConfig task = new TaskConfig();
    private Map<String, AssistantDef> assistants;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public SessionConfig getSession() { return session; }
    public void setSession(SessionConfig session) { this.session = session; }
    public IntentConfig getIntent() { return intent; }
    public void setIntent(IntentConfig intent) { this.intent = intent; }
    public TaskConfig getTask() { return task; }
    public void setTask(TaskConfig task) { this.task = task; }
    public Map<String, AssistantDef> getAssistants() { return assistants; }
    public void setAssistants(Map<String, AssistantDef> assistants) { this.assistants = assistants; }

    public static class SessionConfig {
        private int ttlHours = 24;
        private int maxSessionsPerUser = 50;

        public int getTtlHours() { return ttlHours; }
        public void setTtlHours(int ttlHours) { this.ttlHours = ttlHours; }
        public int getMaxSessionsPerUser() { return maxSessionsPerUser; }
        public void setMaxSessionsPerUser(int maxSessionsPerUser) { this.maxSessionsPerUser = maxSessionsPerUser; }
    }

    public static class IntentConfig {
        private double minConfidence = 0.3;
        private boolean multiIntentEnabled = true;

        public double getMinConfidence() { return minConfidence; }
        public void setMinConfidence(double minConfidence) { this.minConfidence = minConfidence; }
        public boolean isMultiIntentEnabled() { return multiIntentEnabled; }
        public void setMultiIntentEnabled(boolean multiIntentEnabled) { this.multiIntentEnabled = multiIntentEnabled; }
    }

    public static class TaskConfig {
        private int maxRetries = 3;
        private long defaultTimeoutMs = 30000;
        private int maxTasksPerPlan = 10;

        public int getMaxRetries() { return maxRetries; }
        public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
        public long getDefaultTimeoutMs() { return defaultTimeoutMs; }
        public void setDefaultTimeoutMs(long defaultTimeoutMs) { this.defaultTimeoutMs = defaultTimeoutMs; }
        public int getMaxTasksPerPlan() { return maxTasksPerPlan; }
        public void setMaxTasksPerPlan(int maxTasksPerPlan) { this.maxTasksPerPlan = maxTasksPerPlan; }
    }

    public static class AssistantDef {
        private String name;
        private String description;
        private boolean enabled;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}
