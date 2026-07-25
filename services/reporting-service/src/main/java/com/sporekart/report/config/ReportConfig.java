package com.sporekart.report.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "report")
public class ReportConfig {
    private EngineConfig engine = new EngineConfig();
    private TemplateConfig template = new TemplateConfig();
    private ExportConfig export = new ExportConfig();
    private SchedulerConfig scheduler = new SchedulerConfig();
    private CacheConfig cache = new CacheConfig();
    private TelemetryConfig telemetry = new TelemetryConfig();
    private BiConfig bi = new BiConfig();
    private boolean debug = false;

    public EngineConfig getEngine() { return engine; }
    public void setEngine(EngineConfig engine) { this.engine = engine; }
    public TemplateConfig getTemplate() { return template; }
    public void setTemplate(TemplateConfig template) { this.template = template; }
    public ExportConfig getExport() { return export; }
    public void setExport(ExportConfig export) { this.export = export; }
    public SchedulerConfig getScheduler() { return scheduler; }
    public void setScheduler(SchedulerConfig scheduler) { this.scheduler = scheduler; }
    public CacheConfig getCache() { return cache; }
    public void setCache(CacheConfig cache) { this.cache = cache; }
    public TelemetryConfig getTelemetry() { return telemetry; }
    public void setTelemetry(TelemetryConfig telemetry) { this.telemetry = telemetry; }
    public BiConfig getBi() { return bi; }
    public void setBi(BiConfig bi) { this.bi = bi; }
    public boolean isDebug() { return debug; }
    public void setDebug(boolean debug) { this.debug = debug; }

    public static class EngineConfig {
        private boolean enabled = true;
        private String defaultType = "EXECUTIVE";
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getDefaultType() { return defaultType; }
        public void setDefaultType(String defaultType) { this.defaultType = defaultType; }
    }

    public static class TemplateConfig {
        private boolean enabled = true;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class ExportConfig {
        private boolean enabled = true;
        private String defaultFormat = "PDF";
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getDefaultFormat() { return defaultFormat; }
        public void setDefaultFormat(String defaultFormat) { this.defaultFormat = defaultFormat; }
    }

    public static class SchedulerConfig {
        private boolean enabled = true;
        private int maxSchedules = 100;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getMaxSchedules() { return maxSchedules; }
        public void setMaxSchedules(int maxSchedules) { this.maxSchedules = maxSchedules; }
    }

    public static class CacheConfig {
        private boolean enabled = true;
        private int ttlSeconds = 300;
        private int maxSize = 1000;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getTtlSeconds() { return ttlSeconds; }
        public void setTtlSeconds(int ttlSeconds) { this.ttlSeconds = ttlSeconds; }
        public int getMaxSize() { return maxSize; }
        public void setMaxSize(int maxSize) { this.maxSize = maxSize; }
    }

    public static class TelemetryConfig {
        private boolean enabled = true;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class BiConfig {
        private boolean enabled = true;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}
