package com.sporekart.alert.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "alert")
public class AlertConfig {

    private EngineConfig engine = new EngineConfig();
    private RiskConfig risk = new RiskConfig();
    private AnomalyConfig anomaly = new AnomalyConfig();
    private TimelineConfig timeline = new TimelineConfig();
    private CacheConfig cache = new CacheConfig();
    private TelemetryConfig telemetry = new TelemetryConfig();
    private boolean debug = false;

    public EngineConfig getEngine() { return engine; }
    public void setEngine(EngineConfig engine) { this.engine = engine; }
    public RiskConfig getRisk() { return risk; }
    public void setRisk(RiskConfig risk) { this.risk = risk; }
    public AnomalyConfig getAnomaly() { return anomaly; }
    public void setAnomaly(AnomalyConfig anomaly) { this.anomaly = anomaly; }
    public TimelineConfig getTimeline() { return timeline; }
    public void setTimeline(TimelineConfig timeline) { this.timeline = timeline; }
    public CacheConfig getCache() { return cache; }
    public void setCache(CacheConfig cache) { this.cache = cache; }
    public TelemetryConfig getTelemetry() { return telemetry; }
    public void setTelemetry(TelemetryConfig telemetry) { this.telemetry = telemetry; }
    public boolean isDebug() { return debug; }
    public void setDebug(boolean debug) { this.debug = debug; }

    public static class EngineConfig {
        private boolean enabled = true;
        private String defaultSeverity = "MEDIUM";
        private String defaultPriority = "P2";
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getDefaultSeverity() { return defaultSeverity; }
        public void setDefaultSeverity(String s) { this.defaultSeverity = s; }
        public String getDefaultPriority() { return defaultPriority; }
        public void setDefaultPriority(String p) { this.defaultPriority = p; }
    }

    public static class RiskConfig {
        private boolean enabled = true;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class AnomalyConfig {
        private boolean enabled = true;
        private int detectionIntervalSeconds = 60;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getDetectionIntervalSeconds() { return detectionIntervalSeconds; }
        public void setDetectionIntervalSeconds(int s) { this.detectionIntervalSeconds = s; }
    }

    public static class TimelineConfig {
        private boolean enabled = true;
        private int maxEvents = 500;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getMaxEvents() { return maxEvents; }
        public void setMaxEvents(int m) { this.maxEvents = m; }
    }

    public static class CacheConfig {
        private boolean enabled = true;
        private int ttlSeconds = 300;
        private int maxSize = 1000;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getTtlSeconds() { return ttlSeconds; }
        public void setTtlSeconds(int t) { this.ttlSeconds = t; }
        public int getMaxSize() { return maxSize; }
        public void setMaxSize(int m) { this.maxSize = m; }
    }

    public static class TelemetryConfig {
        private boolean enabled = true;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}
