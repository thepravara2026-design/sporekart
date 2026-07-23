package com.sporekart.marketplace.infrastructure.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MarketplaceMetricsService {

    private static final Logger log = LoggerFactory.getLogger(MarketplaceMetricsService.class);

    private final AtomicLong totalQueryLatencyMs = new AtomicLong(0);
    private final AtomicInteger queryCount = new AtomicInteger(0);

    private final AtomicInteger pluginInstalls = new AtomicInteger(0);
    private final AtomicInteger pluginUninstalls = new AtomicInteger(0);
    private final AtomicInteger pluginUpdates = new AtomicInteger(0);
    private final AtomicInteger pluginErrors = new AtomicInteger(0);

    private final ConcurrentHashMap<String, AtomicInteger> actionCounts = new ConcurrentHashMap<>();

    public void recordQueryLatency(long durationMs) {
        totalQueryLatencyMs.addAndGet(durationMs);
        queryCount.incrementAndGet();
    }

    public void recordPluginInstall() {
        pluginInstalls.incrementAndGet();
    }

    public void recordPluginUninstall() {
        pluginUninstalls.incrementAndGet();
    }

    public void recordPluginUpdate() {
        pluginUpdates.incrementAndGet();
    }

    public void recordPluginError() {
        pluginErrors.incrementAndGet();
    }

    public void recordAction(String actionType) {
        actionCounts.computeIfAbsent(actionType, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public double getAverageQueryLatencyMs() {
        int count = queryCount.get();
        return count > 0 ? (double) totalQueryLatencyMs.get() / count : 0;
    }

    public int getPluginInstalls() { return pluginInstalls.get(); }
    public int getPluginUninstalls() { return pluginUninstalls.get(); }
    public int getPluginUpdates() { return pluginUpdates.get(); }
    public int getPluginErrors() { return pluginErrors.get(); }

    public Map<String, Integer> getActionCounts() {
        var map = new java.util.LinkedHashMap<String, Integer>();
        actionCounts.forEach((k, v) -> map.put(k, v.get()));
        return map;
    }

    public Map<String, Object> getAllMetrics() {
        return Map.of(
            "queryLatency", Map.of("avgMs", getAverageQueryLatencyMs(), "total", queryCount.get()),
            "pluginInstalls", pluginInstalls.get(),
            "pluginUninstalls", pluginUninstalls.get(),
            "pluginUpdates", pluginUpdates.get(),
            "pluginErrors", pluginErrors.get(),
            "actionCounts", getActionCounts()
        );
    }

    public void reset() {
        totalQueryLatencyMs.set(0);
        queryCount.set(0);
        pluginInstalls.set(0);
        pluginUninstalls.set(0);
        pluginUpdates.set(0);
        pluginErrors.set(0);
        actionCounts.clear();
    }
}
