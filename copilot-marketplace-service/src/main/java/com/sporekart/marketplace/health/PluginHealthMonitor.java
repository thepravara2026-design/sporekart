package com.sporekart.marketplace.health;

import com.sporekart.marketplace.config.MarketplaceConfig;
import com.sporekart.marketplace.domain.PluginHealthStatus;
import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.registry.PluginRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

@Component
public class PluginHealthMonitor {

    private static final Logger log = LoggerFactory.getLogger(PluginHealthMonitor.class);

    private final PluginRegistry registry;
    private final MarketplaceConfig config;
    private final ConcurrentHashMap<String, PluginHealthStatus> healthStatuses = new ConcurrentHashMap<>();
    private ScheduledFuture<?> healthCheckTask;

    public PluginHealthMonitor(PluginRegistry registry, MarketplaceConfig config) {
        this.registry = registry;
        this.config = config;
    }

    @PostConstruct
    public void start() {
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        healthCheckTask = scheduler.scheduleAtFixedRate(
            this::runHealthChecks,
            0,
            config.getHealth().getCheckIntervalMs(),
            TimeUnit.MILLISECONDS);
        log.info("Plugin health monitor started with interval {}ms", config.getHealth().getCheckIntervalMs());
    }

    @PreDestroy
    public void stop() {
        if (healthCheckTask != null) {
            healthCheckTask.cancel(true);
        }
    }

    public void runHealthChecks() {
        registry.getAll().forEach(this::checkPlugin);
    }

    public PluginHealthStatus checkPlugin(PluginInstance instance) {
        var start = System.currentTimeMillis();
        var pluginId = instance.getManifest().pluginId();
        String status;
        Map<String, Object> details;

        try {
            if (instance.getPlugin() != null) {
                details = instance.getPlugin().healthCheck();
                status = "HEALTHY".equals(details.getOrDefault("status", "UNKNOWN")) ? "HEALTHY" : "UNHEALTHY";
            } else {
                details = Map.of("pluginId", pluginId, "loaded", false);
                status = "UNKNOWN";
            }
        } catch (Exception e) {
            details = Map.of("error", e.getMessage());
            status = "UNHEALTHY";
        }

        var responseTime = System.currentTimeMillis() - start;
        var previous = healthStatuses.get(pluginId);
        var consecutiveFailures = (previous != null && "UNHEALTHY".equals(status))
            ? previous.consecutiveFailures() + 1 : 0;

        var healthStatus = new PluginHealthStatus(pluginId, status, Instant.now(), responseTime, details, consecutiveFailures);
        healthStatuses.put(pluginId, healthStatus);
        return healthStatus;
    }

    public Optional<PluginHealthStatus> getHealth(String pluginId) {
        return Optional.ofNullable(healthStatuses.get(pluginId));
    }

    public List<PluginHealthStatus> getAllHealth() {
        return List.copyOf(healthStatuses.values());
    }

    public long getHealthyCount() {
        return healthStatuses.values().stream().filter(PluginHealthStatus::isHealthy).count();
    }

    public long getUnhealthyCount() {
        return healthStatuses.values().stream().filter(PluginHealthStatus::isUnhealthy).count();
    }

    public Map<String, Object> getHealthSummary() {
        var all = healthStatuses.values();
        var healthy = all.stream().filter(PluginHealthStatus::isHealthy).count();
        var unhealthy = all.stream().filter(PluginHealthStatus::isUnhealthy).count();
        var unknown = all.size() - healthy - unhealthy;
        return Map.of(
            "total", all.size(),
            "healthy", healthy,
            "unhealthy", unhealthy,
            "unknown", unknown,
            "averageResponseTimeMs", all.stream().mapToLong(PluginHealthStatus::responseTimeMs).average().orElse(0)
        );
    }
}
