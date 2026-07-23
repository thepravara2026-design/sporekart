package com.sporekart.marketplace.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.marketplace")
public class MarketplaceConfig {

    private String name = "SporeKart Copilot Marketplace";
    private String version = "1.0.0";
    private String pluginStorePath = "./plugins";
    private String maxPluginSize = "100MB";
    private int maxInstalledPlugins = 100;
    private SandboxConfig sandbox = new SandboxConfig();
    private HealthConfig health = new HealthConfig();

    public static class SandboxConfig {
        private boolean enabled = true;
        private String maxMemoryPerPlugin = "256MB";
        private long maxExecutionTimeoutMs = 30000;
        private int maxThreadsPerPlugin = 5;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getMaxMemoryPerPlugin() { return maxMemoryPerPlugin; }
        public void setMaxMemoryPerPlugin(String maxMemoryPerPlugin) { this.maxMemoryPerPlugin = maxMemoryPerPlugin; }
        public long getMaxExecutionTimeoutMs() { return maxExecutionTimeoutMs; }
        public void setMaxExecutionTimeoutMs(long maxExecutionTimeoutMs) { this.maxExecutionTimeoutMs = maxExecutionTimeoutMs; }
        public int getMaxThreadsPerPlugin() { return maxThreadsPerPlugin; }
        public void setMaxThreadsPerPlugin(int maxThreadsPerPlugin) { this.maxThreadsPerPlugin = maxThreadsPerPlugin; }
    }

    public static class HealthConfig {
        private long checkIntervalMs = 60000;
        private int failureThreshold = 3;
        private int recoveryThreshold = 2;

        public long getCheckIntervalMs() { return checkIntervalMs; }
        public void setCheckIntervalMs(long checkIntervalMs) { this.checkIntervalMs = checkIntervalMs; }
        public int getFailureThreshold() { return failureThreshold; }
        public void setFailureThreshold(int failureThreshold) { this.failureThreshold = failureThreshold; }
        public int getRecoveryThreshold() { return recoveryThreshold; }
        public void setRecoveryThreshold(int recoveryThreshold) { this.recoveryThreshold = recoveryThreshold; }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getPluginStorePath() { return pluginStorePath; }
    public void setPluginStorePath(String pluginStorePath) { this.pluginStorePath = pluginStorePath; }
    public String getMaxPluginSize() { return maxPluginSize; }
    public void setMaxPluginSize(String maxPluginSize) { this.maxPluginSize = maxPluginSize; }
    public int getMaxInstalledPlugins() { return maxInstalledPlugins; }
    public void setMaxInstalledPlugins(int maxInstalledPlugins) { this.maxInstalledPlugins = maxInstalledPlugins; }
    public SandboxConfig getSandbox() { return sandbox; }
    public void setSandbox(SandboxConfig sandbox) { this.sandbox = sandbox; }
    public HealthConfig getHealth() { return health; }
    public void setHealth(HealthConfig health) { this.health = health; }
}