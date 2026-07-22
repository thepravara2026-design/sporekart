package com.sporekart.memory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sporekart.memory")
public class MemoryConfig {

    private List<String> allowedOrigins = List.of("*");
    private boolean encryptionEnabled = true;
    private int defaultMaxResults = 20;
    private double defaultMinRelevanceScore = 0.5;
    private RetentionConfig retention = new RetentionConfig();
    private VectorStoreConfig vectorStore = new VectorStoreConfig();

    public List<String> getAllowedOrigins() { return allowedOrigins; }
    public void setAllowedOrigins(List<String> allowedOrigins) { this.allowedOrigins = allowedOrigins; }

    public boolean isEncryptionEnabled() { return encryptionEnabled; }
    public void setEncryptionEnabled(boolean encryptionEnabled) { this.encryptionEnabled = encryptionEnabled; }

    public int getDefaultMaxResults() { return defaultMaxResults; }
    public void setDefaultMaxResults(int defaultMaxResults) { this.defaultMaxResults = defaultMaxResults; }

    public double getDefaultMinRelevanceScore() { return defaultMinRelevanceScore; }
    public void setDefaultMinRelevanceScore(double defaultMinRelevanceScore) { this.defaultMinRelevanceScore = defaultMinRelevanceScore; }

    public RetentionConfig getRetention() { return retention; }
    public void setRetention(RetentionConfig retention) { this.retention = retention; }

    public VectorStoreConfig getVectorStore() { return vectorStore; }
    public void setVectorStore(VectorStoreConfig vectorStore) { this.vectorStore = vectorStore; }

    public static class RetentionConfig {
        private int ephemeralTtlHours = 24;
        private int defaultTtlDays = 90;
        private int maxTtlDays = 365;

        public int getEphemeralTtlHours() { return ephemeralTtlHours; }
        public void setEphemeralTtlHours(int v) { this.ephemeralTtlHours = v; }

        public int getDefaultTtlDays() { return defaultTtlDays; }
        public void setDefaultTtlDays(int v) { this.defaultTtlDays = v; }

        public int getMaxTtlDays() { return maxTtlDays; }
        public void setMaxTtlDays(int v) { this.maxTtlDays = v; }
    }

    public static class VectorStoreConfig {
        private String provider = "MEMORY";
        private int dimensions = 1536;
        private String indexType = "HNSW";

        public String getProvider() { return provider; }
        public void setProvider(String v) { this.provider = v; }

        public int getDimensions() { return dimensions; }
        public void setDimensions(int v) { this.dimensions = v; }

        public String getIndexType() { return indexType; }
        public void setIndexType(String v) { this.indexType = v; }
    }
}
