package com.sporekart.ai.memory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.modules.memory")
public class MemoryConfig {

    private boolean enabled = true;
    private int defaultMaxResults = 50;
    private Duration defaultTtl = Duration.ofDays(30);
    private Consolidation consolidation = new Consolidation();
    private Indexing indexing = new Indexing();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getDefaultMaxResults() {
        return defaultMaxResults;
    }

    public void setDefaultMaxResults(int defaultMaxResults) {
        this.defaultMaxResults = defaultMaxResults;
    }

    public Duration getDefaultTtl() {
        return defaultTtl;
    }

    public void setDefaultTtl(Duration defaultTtl) {
        this.defaultTtl = defaultTtl;
    }

    public Consolidation getConsolidation() {
        return consolidation;
    }

    public void setConsolidation(Consolidation consolidation) {
        this.consolidation = consolidation;
    }

    public Indexing getIndexing() {
        return indexing;
    }

    public void setIndexing(Indexing indexing) {
        this.indexing = indexing;
    }

    public static class Consolidation {
        private boolean enabled = true;
        private String schedule = "0 0 2 * * ?";
        private int retentionDays = 90;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }

        public int getRetentionDays() {
            return retentionDays;
        }

        public void setRetentionDays(int retentionDays) {
            this.retentionDays = retentionDays;
        }
    }

    public static class Indexing {
        private boolean enabled = true;
        private String vectorDimension = "1536";
        private String indexType = "HNSW";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getVectorDimension() {
            return vectorDimension;
        }

        public void setVectorDimension(String vectorDimension) {
            this.vectorDimension = vectorDimension;
        }

        public String getIndexType() {
            return indexType;
        }

        public void setIndexType(String indexType) {
            this.indexType = indexType;
        }
    }
}
