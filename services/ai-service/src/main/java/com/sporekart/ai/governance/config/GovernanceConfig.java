package com.sporekart.ai.governance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "governance")
@Data
public class GovernanceConfig {
    private boolean enabled = true;
    private String mode = "DEVELOPMENT";
    private String defaultDecision = "ALLOW";
    private CacheConfig cache = new CacheConfig();
    private KafkaConfig kafka = new KafkaConfig();

    @Data
    public static class CacheConfig {
        private int configTtlSeconds = 300;
        private int registryTtlSeconds = 300;
        private int healthTtlSeconds = 60;
        private int metricsTtlSeconds = 120;
        private int validationTtlSeconds = 180;
    }

    @Data
    public static class KafkaConfig {
        private String topic = "governance-events";
        private int partitions = 3;
        private short replicationFactor = 1;
    }
}
