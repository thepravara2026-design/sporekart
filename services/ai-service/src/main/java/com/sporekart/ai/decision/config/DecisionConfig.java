package com.sporekart.ai.decision.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "decision")
@Data
public class DecisionConfig {
    private boolean enabled = true;
    private String defaultAction = "ALLOW";
    private String defaultConflictStrategy = "DENY_OVERRIDES";
    private CacheConfig cache = new CacheConfig();
    private KafkaConfig kafka = new KafkaConfig();

    @Data public static class CacheConfig {
        private int resultTtlSeconds = 300;
        private int metadataTtlSeconds = 300;
        private int registryTtlSeconds = 300;
        private int statisticsTtlSeconds = 120;
        private int explanationTtlSeconds = 300;
    }

    @Data public static class KafkaConfig {
        private String topic = "decision-events";
        private int partitions = 3;
        private short replicationFactor = 1;
    }
}
