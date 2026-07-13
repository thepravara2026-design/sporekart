package com.sporekart.ai.automation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "automation")
public class AutomationConfig {

    private boolean enabled = true;
    private SchedulerConfig scheduler = new SchedulerConfig();
    private RetryConfig retry = new RetryConfig();
    private CacheConfig cache = new CacheConfig();
    private KafkaConfig kafka = new KafkaConfig();

    @Data
    public static class SchedulerConfig {
        private boolean enabled = true;
        private long pollIntervalMs = 60000;
    }

    @Data
    public static class RetryConfig {
        private int defaultMaxRetries = 3;
        private long defaultBackoffMs = 1000;
        private double defaultBackoffMultiplier = 2.0;
    }

    @Data
    public static class CacheConfig {
        private int workflowTtl = 300;
        private int schedulerTtl = 300;
        private int lifecycleTtl = 600;
        private int configTtl = 300;
        private int statsTtl = 120;
    }

    @Data
    public static class KafkaConfig {
        private String topic = "automation-events";
        private int partitions = 3;
        private int replicationFactor = 1;
    }
}
