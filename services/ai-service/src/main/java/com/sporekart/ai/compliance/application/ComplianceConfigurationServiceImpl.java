package com.sporekart.ai.compliance.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ComplianceConfigurationServiceImpl implements ComplianceConfigurationService {

    private final ConcurrentHashMap<String, String> configStore = new ConcurrentHashMap<>();

    @Override
    public Optional<String> getConfig(String key) {
        return Optional.ofNullable(configStore.get(key));
    }

    @Override
    public void setConfig(String key, String value) {
        configStore.put(key, value);
        log.info("Configuration set: {} = {}", key, value);
    }

    @Override
    public Map<String, String> getAllConfigs() {
        return Map.copyOf(configStore);
    }

    @Override
    public void reloadConfig() {
        log.info("Configuration reloaded");
    }

    @Override
    public boolean isFeatureEnabled(String feature) {
        return "true".equalsIgnoreCase(configStore.getOrDefault(feature, "true"));
    }
}
