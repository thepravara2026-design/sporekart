package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.ProviderHealth;
import com.sporekart.ai.core.api.ProviderHealthService;
import com.sporekart.ai.provider.api.ProviderHealthIndicator;
import com.sporekart.ai.provider.domain.ProviderHealthRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ProviderHealthServiceImpl implements ProviderHealthService {
    private static final Logger log = LoggerFactory.getLogger(ProviderHealthServiceImpl.class);

    private final ProviderRegistryImpl registry;
    private final Map<String, ProviderHealthRecord> healthCache = new ConcurrentHashMap<>();

    public ProviderHealthServiceImpl(ProviderRegistryImpl registry) {
        this.registry = registry;
    }

    @Override
    public ProviderHealth checkHealth(String providerName) {
        ProviderHealthRecord record = healthCache.get(providerName);
        if (record != null) {
            return toProviderHealth(record);
        }
        return ProviderHealth.healthy(providerName);
    }

    @Override
    public List<ProviderHealth> checkAllProviders() {
        return registry.getRegisteredProviders().stream()
                .map(p -> checkHealth(p.providerType()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isProviderHealthy(String providerName) {
        ProviderHealthRecord record = healthCache.get(providerName);
        return record == null || record.healthy();
    }

    @Override
    public void markHealthChanged(String providerName, boolean healthy, String details) {
        ProviderHealthRecord current = healthCache.get(providerName);
        int consecutiveFailures = 0;
        OffsetDateTime lastFailure = null;

        if (current != null && !healthy) {
            consecutiveFailures = current.consecutiveFailures() + 1;
            lastFailure = OffsetDateTime.now();
        }

        ProviderHealthRecord record = new ProviderHealthRecord(
                providerName, healthy, !healthy, 0,
                OffsetDateTime.now(), lastFailure, consecutiveFailures, details);

        healthCache.put(providerName, record);

        if (!healthy) {
            log.warn("Provider {} marked unhealthy: {}", providerName, details);
        }
    }

    public void recordHealthCheck(String providerName, boolean healthy, long latencyMs) {
        ProviderHealthRecord current = healthCache.get(providerName);
        int consecutiveFailures = 0;
        OffsetDateTime lastFailure = null;

        if (current != null) {
            consecutiveFailures = healthy ? 0 : current.consecutiveFailures() + 1;
            if (!healthy) {
                lastFailure = OffsetDateTime.now();
            } else {
                lastFailure = current.lastFailure();
            }
        }

        ProviderHealthRecord record = new ProviderHealthRecord(
                providerName, healthy, latencyMs > 5000, latencyMs,
                OffsetDateTime.now(), lastFailure, consecutiveFailures,
                healthy ? "Operational" : "Unhealthy");

        healthCache.put(providerName, record);
    }

    public List<ProviderHealthRecord> getHealthRecords() {
        return List.copyOf(healthCache.values());
    }

    private ProviderHealth toProviderHealth(ProviderHealthRecord record) {
        return new ProviderHealth(
                record.providerType(),
                record.healthy(),
                record.degraded(),
                record.latencyMs(),
                record.lastChecked(),
                record.details());
    }
}
