package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.AutomationConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AutomationConfigurationServiceImpl implements AutomationConfigurationService {

    private final ConcurrentHashMap<String, Object> configStore = new ConcurrentHashMap<>();

    public AutomationConfigurationServiceImpl() {
        configStore.put("automation.enabled", true);
        configStore.put("automation.scheduler.pollIntervalMs", 60000);
        configStore.put("automation.retry.maxRetries", 3);
        configStore.put("automation.retry.backoffMs", 1000);
        configStore.put("automation.retry.backoffMultiplier", 2.0);
    }

    @Override
    public Object getConfig(String key) {
        return configStore.get(key);
    }

    @Override
    public void setConfig(String key, Object value) {
        configStore.put(key, value);
        log.info("Configuration updated: {} = {}", key, value);
    }

    @Override
    public Map<String, Object> getAllConfigs() {
        return Map.copyOf(configStore);
    }

    @Override
    public void reloadConfig() {
        configStore.clear();
        configStore.put("automation.enabled", true);
        configStore.put("automation.scheduler.pollIntervalMs", 60000);
        configStore.put("automation.retry.maxRetries", 3);
        configStore.put("automation.retry.backoffMs", 1000);
        configStore.put("automation.retry.backoffMultiplier", 2.0);
        log.info("Configuration reloaded to defaults");
    }

    @Override
    public boolean isFeatureEnabled(String feature) {
        var value = configStore.get(feature);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }
}
