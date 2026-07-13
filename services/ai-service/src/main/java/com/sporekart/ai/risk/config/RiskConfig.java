package com.sporekart.ai.risk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "risk")
public class RiskConfig {

    private boolean enabled = true;

    private ScoringConfig scoring = new ScoringConfig();
    private TrustConfig trust = new TrustConfig();
    private ConfidenceConfig confidence = new ConfidenceConfig();
    private CacheConfig cache = new CacheConfig();
    private KafkaConfig kafka = new KafkaConfig();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ScoringConfig getScoring() {
        return scoring;
    }

    public void setScoring(ScoringConfig scoring) {
        this.scoring = scoring;
    }

    public TrustConfig getTrust() {
        return trust;
    }

    public void setTrust(TrustConfig trust) {
        this.trust = trust;
    }

    public ConfidenceConfig getConfidence() {
        return confidence;
    }

    public void setConfidence(ConfidenceConfig confidence) {
        this.confidence = confidence;
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

    public static class ScoringConfig {
        private double defaultRiskScore = 50.0;

        public double getDefaultRiskScore() { return defaultRiskScore; }
        public void setDefaultRiskScore(double defaultRiskScore) { this.defaultRiskScore = defaultRiskScore; }
    }

    public static class TrustConfig {
        private double defaultTrustScore = 75.0;

        public double getDefaultTrustScore() { return defaultTrustScore; }
        public void setDefaultTrustScore(double defaultTrustScore) { this.defaultTrustScore = defaultTrustScore; }
    }

    public static class ConfidenceConfig {
        private double defaultConfidence = 70.0;

        public double getDefaultConfidence() { return defaultConfidence; }
        public void setDefaultConfidence(double defaultConfidence) { this.defaultConfidence = defaultConfidence; }
    }

    public static class CacheConfig {
        private int scoresTtl = 300;
        private int trustTtl = 300;
        private int confidenceTtl = 300;
        private int thresholdsTtl = 600;
        private int metadataTtl = 300;

        public int getScoresTtl() { return scoresTtl; }
        public void setScoresTtl(int scoresTtl) { this.scoresTtl = scoresTtl; }
        public int getTrustTtl() { return trustTtl; }
        public void setTrustTtl(int trustTtl) { this.trustTtl = trustTtl; }
        public int getConfidenceTtl() { return confidenceTtl; }
        public void setConfidenceTtl(int confidenceTtl) { this.confidenceTtl = confidenceTtl; }
        public int getThresholdsTtl() { return thresholdsTtl; }
        public void setThresholdsTtl(int thresholdsTtl) { this.thresholdsTtl = thresholdsTtl; }
        public int getMetadataTtl() { return metadataTtl; }
        public void setMetadataTtl(int metadataTtl) { this.metadataTtl = metadataTtl; }
    }

    public static class KafkaConfig {
        private String topic = "risk-events";
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
