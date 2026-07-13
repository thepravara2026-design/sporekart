package com.sporekart.ai.analytics.application;

import com.sporekart.ai.analytics.api.AnalyticsConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsConfigurationServiceImpl implements AnalyticsConfigurationService {

    private final ConcurrentHashMap<String, Object> configStore = new ConcurrentHashMap<>();

    {
        configStore.put("analytics.enabled", true);
        configStore.put("analytics.retention.days", 90);
        configStore.put("analytics.cache.enabled", true);
        configStore.put("analytics.reports.maxExportSize", 10485760);
        configStore.put("analytics.feature.dashboard", true);
        configStore.put("analytics.feature.reporting", true);
        configStore.put("analytics.feature.trends", true);
        configStore.put("analytics.feature.kpi", true);
        configStore.put("analytics.feature.export", true);
    }

    @Override
    public Object getConfig(String key) {
        return configStore.get(key);
    }

    @Override
    public void setConfig(String key, Object value) {
        configStore.put(key, value);
        log.info("Configuration '{}' set to {}", key, value);
    }

    @Override
    public Map<String, Object> getAllConfigs() {
        return Map.copyOf(configStore);
    }

    @Override
    public void reloadConfig() {
        log.info("Configuration reload triggered (in-memory store, no-op)");
    }

    @Override
    public boolean isFeatureEnabled(String feature) {
        var key = "analytics.feature." + feature;
        var value = configStore.get(key);
        return value instanceof Boolean && (Boolean) value;
    }
}
