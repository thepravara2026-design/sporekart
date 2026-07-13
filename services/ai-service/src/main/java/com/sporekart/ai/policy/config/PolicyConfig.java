package com.sporekart.ai.policy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "policy")
@Data
public class PolicyConfig {
    private boolean enabled = true;
    private String defaultDecision = "ALLOW";
    private String defaultConflictStrategy = "DENY_OVERRIDES";
    private CacheConfig cache = new CacheConfig();
    private KafkaConfig kafka = new KafkaConfig();

    @Data
    public static class CacheConfig {
        private int registryTtlSeconds = 300;
        private int compiledTtlSeconds = 600;
        private int metadataTtlSeconds = 300;
        private int evaluationTtlSeconds = 180;
        private int healthTtlSeconds = 60;
    }

    @Data
    public static class KafkaConfig {
        private String topic = "policy-events";
        private int partitions = 3;
        private short replicationFactor = 1;
    }
}
