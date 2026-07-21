package com.sporekart.ai.provider.health;

import com.sporekart.ai.provider.interfaces.AIProvider;
import com.sporekart.ai.provider.models.ProviderHealth;
import com.sporekart.ai.provider.registry.ProviderRegistry;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HealthMonitor {
    private final ProviderRegistry registry;
    private final Map<String, HeartbeatTracker> heartbeats = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final long checkIntervalMs;

    public HealthMonitor(ProviderRegistry registry, long checkIntervalMs) {
        this.registry = registry;
        this.checkIntervalMs = checkIntervalMs;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::performHealthChecks,
                0, checkIntervalMs, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }

    public ProviderHealth checkProviderHealth(String providerId) {
        var providerOpt = registry.lookup(providerId);
        if (providerOpt.isEmpty()) {
            return ProviderHealth.offline(providerId, "unknown");
        }
        return performHealthCheck(providerOpt.get());
    }

    public ProviderHealth checkProviderHealth(AIProvider provider) {
        return performHealthCheck(provider);
    }

    public List<ProviderHealth> checkAllProviders() {
        return registry.getAllProviders().stream()
                .map(this::performHealthCheck)
                .collect(Collectors.toList());
    }

    public ProviderHealth.HealthStatus getAggregateHealth() {
        var all = registry.getAllProviders();
        if (all.isEmpty()) return ProviderHealth.HealthStatus.UNKNOWN;

        boolean anyHealthy = false;
        boolean anyDegraded = false;
        boolean anyUnhealthy = false;

        for (var provider : all) {
            var health = heartbeats.get(provider.providerId());
            if (health == null) continue;
            switch (health.lastStatus) {
                case HEALTHY -> anyHealthy = true;
                case DEGRADED -> anyDegraded = true;
                case UNHEALTHY, OFFLINE -> anyUnhealthy = true;
            }
        }

        if (anyHealthy && !anyDegraded && !anyUnhealthy) return ProviderHealth.HealthStatus.HEALTHY;
        if (anyDegraded) return ProviderHealth.HealthStatus.DEGRADED;
        if (anyUnhealthy && !anyHealthy) return ProviderHealth.HealthStatus.UNHEALTHY;
        return ProviderHealth.HealthStatus.DEGRADED;
    }

    public ProviderHealth getLastHealth(String providerId) {
        var tracker = heartbeats.get(providerId);
        if (tracker == null) return ProviderHealth.unknown();
        return tracker.toHealth();
    }

    public Map<String, ProviderHealth> getAllLastHealth() {
        return heartbeats.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toHealth()));
    }

    private void performHealthChecks() {
        for (var provider : registry.getAllProviders()) {
            try {
                var health = performHealthCheck(provider);
                registry.updateHealth(provider.providerId(), health);
            } catch (Exception e) {
                var health = ProviderHealth.unhealthy(
                        provider.providerId(), provider.providerName(), e.getMessage());
                registry.updateHealth(provider.providerId(), health);
            }
        }
    }

    private ProviderHealth performHealthCheck(AIProvider provider) {
        var start = Instant.now();
        var health = provider.checkHealth();
        var latency = Duration.between(start, Instant.now());

        var tracker = heartbeats.computeIfAbsent(provider.providerId(),
                id -> new HeartbeatTracker());
        tracker.record(health.status(), latency);

        return new ProviderHealth(
                provider.providerId(),
                provider.providerName(),
                health.status(),
                latency,
                Instant.now(),
                health.lastFailure(),
                health.consecutiveFailures(),
                health.uptimeSeconds(),
                health.details());
    }

    private static class HeartbeatTracker {
        private ProviderHealth.HealthStatus lastStatus = ProviderHealth.HealthStatus.UNKNOWN;
        private Instant lastHeartbeat = Instant.now();
        private int consecutiveFailures = 0;
        private long totalUptimeSeconds = 0;
        private Duration lastLatency = Duration.ZERO;

        void record(ProviderHealth.HealthStatus status, Duration latency) {
            if (status == ProviderHealth.HealthStatus.HEALTHY
                    || status == ProviderHealth.HealthStatus.DEGRADED) {
                consecutiveFailures = 0;
            } else {
                consecutiveFailures++;
            }
            lastStatus = status;
            lastHeartbeat = Instant.now();
            lastLatency = latency;
        }

        ProviderHealth toHealth() {
            return new ProviderHealth("", "", lastStatus, lastLatency,
                    lastHeartbeat, null, consecutiveFailures,
                    totalUptimeSeconds, "");
        }
    }
}
