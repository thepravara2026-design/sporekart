package com.sporekart.ai.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "admin")
public class AdminConfig {

    private boolean enabled = true;
    private CacheConfig cache = new CacheConfig();
    private KafkaConfig kafka = new KafkaConfig();

    @Data
    public static class CacheConfig {
        private int configTtl = 300;
        private int flagsTtl = 300;
        private int envTtl = 600;
        private int snapshotTtl = 600;
        private int metadataTtl = 300;
    }

    @Data
    public static class KafkaConfig {
        private String topic = "admin-events";
        private int partitions = 3;
        private int replicationFactor = 1;
    }
}
