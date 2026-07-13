package com.sporekart.ai.analytics.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "analytics")
public class AnalyticsConfig {

    private boolean enabled = true;

    private CacheConfig cache = new CacheConfig();
    private KafkaConfig kafka = new KafkaConfig();
    private ExportConfig export = new ExportConfig();

    @Getter
    @Setter
    public static class CacheConfig {
        private int dashboardTtl = 300;
        private int metricsTtl = 120;
        private int kpisTtl = 300;
        private int reportsTtl = 300;
        private int statsTtl = 120;
    }

    @Getter
    @Setter
    public static class KafkaConfig {
        private String topic = "analytics-events";
        private int partitions = 3;
        private int replicationFactor = 1;
    }

    @Getter
    @Setter
    public static class ExportConfig {
        private List<String> supportedFormats = List.of("JSON", "CSV");
        private long maxFileSize = 10485760;
    }
}
