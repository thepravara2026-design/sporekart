package com.sporekart.workspace.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "sporekart.workspace")
public class WorkspaceConfig {

    private boolean enabled = true;
    private int maxSessionsPerUser = 5;
    private int sessionTimeoutMinutes = 60;
    private int streamingBufferSize = 4096;
    private Routing routing = new Routing();
    private Collaboration collaboration = new Collaboration();
    private Memory memory = new Memory();
    private Map<String, CopilotInstance> copilots;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxSessionsPerUser() {
        return maxSessionsPerUser;
    }

    public void setMaxSessionsPerUser(int maxSessionsPerUser) {
        this.maxSessionsPerUser = maxSessionsPerUser;
    }

    public int getSessionTimeoutMinutes() {
        return sessionTimeoutMinutes;
    }

    public void setSessionTimeoutMinutes(int sessionTimeoutMinutes) {
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
    }

    public int getStreamingBufferSize() {
        return streamingBufferSize;
    }

    public void setStreamingBufferSize(int streamingBufferSize) {
        this.streamingBufferSize = streamingBufferSize;
    }

    public Routing getRouting() {
        return routing;
    }

    public void setRouting(Routing routing) {
        this.routing = routing;
    }

    public Collaboration getCollaboration() {
        return collaboration;
    }

    public void setCollaboration(Collaboration collaboration) {
        this.collaboration = collaboration;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Map<String, CopilotInstance> getCopilots() {
        return copilots;
    }

    public void setCopilots(Map<String, CopilotInstance> copilots) {
        this.copilots = copilots;
    }

    public static class Routing {
        private boolean enableAutomaticRouting = true;
        private double confidenceThreshold = 0.6;
        private boolean enableFallback = true;
        private String fallbackCopilot = "admin";

        public boolean isEnableAutomaticRouting() {
            return enableAutomaticRouting;
        }

        public void setEnableAutomaticRouting(boolean enableAutomaticRouting) {
            this.enableAutomaticRouting = enableAutomaticRouting;
        }

        public double getConfidenceThreshold() {
            return confidenceThreshold;
        }

        public void setConfidenceThreshold(double confidenceThreshold) {
            this.confidenceThreshold = confidenceThreshold;
        }

        public boolean isEnableFallback() {
            return enableFallback;
        }

        public void setEnableFallback(boolean enableFallback) {
            this.enableFallback = enableFallback;
        }

        public String getFallbackCopilot() {
            return fallbackCopilot;
        }

        public void setFallbackCopilot(String fallbackCopilot) {
            this.fallbackCopilot = fallbackCopilot;
        }
    }

    public static class Collaboration {
        private int maxCollaborationDepth = 3;
        private int timeoutSeconds = 30;

        public int getMaxCollaborationDepth() {
            return maxCollaborationDepth;
        }

        public void setMaxCollaborationDepth(int maxCollaborationDepth) {
            this.maxCollaborationDepth = maxCollaborationDepth;
        }

        public int getTimeoutSeconds() {
            return timeoutSeconds;
        }

        public void setTimeoutSeconds(int timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
        }
    }

    public static class Memory {
        private int maxSharedEntries = 1000;
        private int ttlMinutes = 1440;

        public int getMaxSharedEntries() {
            return maxSharedEntries;
        }

        public void setMaxSharedEntries(int maxSharedEntries) {
            this.maxSharedEntries = maxSharedEntries;
        }

        public int getTtlMinutes() {
            return ttlMinutes;
        }

        public void setTtlMinutes(int ttlMinutes) {
            this.ttlMinutes = ttlMinutes;
        }
    }

    public static class CopilotInstance {
        private String url;
        private boolean enabled = true;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
