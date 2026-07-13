package com.sporekart.ai.approval.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "approval")
public class ApprovalConfig {

    private boolean enabled = true;
    private int defaultSlaMinutes = 60;
    private int maxLevels = 3;

    private CacheConfig cache = new CacheConfig();
    private KafkaConfig kafka = new KafkaConfig();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getDefaultSlaMinutes() {
        return defaultSlaMinutes;
    }

    public void setDefaultSlaMinutes(int defaultSlaMinutes) {
        this.defaultSlaMinutes = defaultSlaMinutes;
    }

    public int getMaxLevels() {
        return maxLevels;
    }

    public void setMaxLevels(int maxLevels) {
        this.maxLevels = maxLevels;
    }

    public CacheConfig getCache() {
        return cache;
    }

    public void setCache(CacheConfig cache) {
        this.cache = cache;
    }

    public KafkaConfig getKafka() {
        return kafka;
    }

    public void setKafka(KafkaConfig kafka) {
        this.kafka = kafka;
    }

    public static class CacheConfig {
        private int pendingTtl = 60;
        private int assignmentTtl = 120;
        private int configTtl = 300;
        private int workflowTtl = 300;
        private int statsTtl = 120;

        public int getPendingTtl() { return pendingTtl; }
        public void setPendingTtl(int pendingTtl) { this.pendingTtl = pendingTtl; }
        public int getAssignmentTtl() { return assignmentTtl; }
        public void setAssignmentTtl(int assignmentTtl) { this.assignmentTtl = assignmentTtl; }
        public int getConfigTtl() { return configTtl; }
        public void setConfigTtl(int configTtl) { this.configTtl = configTtl; }
        public int getWorkflowTtl() { return workflowTtl; }
        public void setWorkflowTtl(int workflowTtl) { this.workflowTtl = workflowTtl; }
        public int getStatsTtl() { return statsTtl; }
        public void setStatsTtl(int statsTtl) { this.statsTtl = statsTtl; }
    }

    public static class KafkaConfig {
        private String topic = "approval-events";
        private int partitions = 3;
        private int replicationFactor = 1;

        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public int getPartitions() { return partitions; }
        public void setPartitions(int partitions) { this.partitions = partitions; }
        public int getReplicationFactor() { return replicationFactor; }
        public void setReplicationFactor(int replicationFactor) { this.replicationFactor = replicationFactor; }
    }
}
