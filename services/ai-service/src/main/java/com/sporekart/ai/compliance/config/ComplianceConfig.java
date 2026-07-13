package com.sporekart.ai.compliance.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "compliance")
public class ComplianceConfig {

    private boolean enabled = true;
    private String defaultFramework = "INTERNAL_AI_GOVERNANCE";
    private boolean defaultEvidenceVerification = true;

    private CacheConfig cache = new CacheConfig();
    private KafkaConfig kafka = new KafkaConfig();

    @Getter
    @Setter
    public static class CacheConfig {
        private int rulesTtl = 300;
        private int frameworksTtl = 300;
        private int validationTtl = 180;
        private int reportsTtl = 300;
        private int statsTtl = 120;
    }

    @Getter
    @Setter
    public static class KafkaConfig {
        private String topic = "compliance-events";
        private int partitions = 3;
        private int replicationFactor = 1;
    }
}
