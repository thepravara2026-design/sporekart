package com.sporekart.marketplace.sandbox;

import com.sporekart.marketplace.config.MarketplaceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ResourceLimiter {

    private static final Logger log = LoggerFactory.getLogger(ResourceLimiter.class);

    private final MarketplaceConfig config;
    private final ConcurrentHashMap<String, PluginResourceUsage> usage = new ConcurrentHashMap<>();

    public ResourceLimiter(MarketplaceConfig config) {
        this.config = config;
    }

    public boolean allocateResources(String pluginId) {
        var totalActive = usage.size();
        if (totalActive >= config.getMaxInstalledPlugins()) {
            log.warn("Resource limit reached: cannot allocate for {}", pluginId);
            return false;
        }
        usage.computeIfAbsent(pluginId, k -> new PluginResourceUsage());
        return true;
    }

    public void releaseResources(String pluginId) {
        usage.remove(pluginId);
    }

    public boolean isWithinLimits(String pluginId) {
        var res = usage.get(pluginId);
        if (res == null) return false;
        return res.threadCount.get() <= config.getSandbox().getMaxThreadsPerPlugin();
    }

    public PluginResourceUsage getUsage(String pluginId) {
        return usage.get(pluginId);
    }

    public int getActivePluginCount() { return usage.size(); }

    public static class PluginResourceUsage {
        private final AtomicInteger threadCount = new AtomicInteger(0);
        private final AtomicInteger executionCount = new AtomicInteger(0);
        private long totalExecutionTimeMs = 0;

        public void incrementThreads() { threadCount.incrementAndGet(); }
        public void decrementThreads() { threadCount.decrementAndGet(); }
        public int getThreadCount() { return threadCount.get(); }
        public void incrementExecutions() { executionCount.incrementAndGet(); }
        public int getExecutionCount() { return executionCount.get(); }
        public void addExecutionTime(long ms) { totalExecutionTimeMs += ms; }
        public long getTotalExecutionTimeMs() { return totalExecutionTimeMs; }
    }
}
