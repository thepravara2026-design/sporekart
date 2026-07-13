package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.RiskConfigurationService;
import com.sporekart.ai.risk.domain.RiskLevel;
import com.sporekart.ai.risk.domain.RiskThreshold;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RiskConfigurationServiceImpl implements RiskConfigurationService {

    private final ConcurrentHashMap<String, Object> configStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<RiskLevel, RiskThreshold> thresholds = new ConcurrentHashMap<>();

    public RiskConfigurationServiceImpl() {
        thresholds.put(RiskLevel.LOW, new RiskThreshold(UUID.randomUUID(), RiskLevel.LOW, 0, 20, "proceed"));
        thresholds.put(RiskLevel.MEDIUM, new RiskThreshold(UUID.randomUUID(), RiskLevel.MEDIUM, 21, 40, "retry"));
        thresholds.put(RiskLevel.HIGH, new RiskThreshold(UUID.randomUUID(), RiskLevel.HIGH, 41, 70, "require_approval"));
        thresholds.put(RiskLevel.CRITICAL, new RiskThreshold(UUID.randomUUID(), RiskLevel.CRITICAL, 71, 100, "block"));

        configStore.put("enabled", true);
        configStore.put("defaultRiskScore", 50.0);
        configStore.put("defaultTrustScore", 75.0);
        configStore.put("defaultConfidence", 70.0);
        log.info("Risk configuration initialized with default thresholds");
    }

    @Override
    public Object getConfig(String key) {
        return configStore.get(key);
    }

    @Override
    public void setConfig(String key, Object value) {
        configStore.put(key, value);
        log.info("Configuration set: {} = {}", key, value);
    }

    @Override
    public Map<String, Object> getAllConfigs() {
        Map<String, Object> all = new ConcurrentHashMap<>(configStore);
        thresholds.forEach((level, threshold) ->
            all.put("threshold." + level.name().toLowerCase(), threshold));
        return all;
    }

    @Override
    public void reloadConfig() {
        configStore.clear();
        thresholds.clear();
        thresholds.put(RiskLevel.LOW, new RiskThreshold(UUID.randomUUID(), RiskLevel.LOW, 0, 20, "proceed"));
        thresholds.put(RiskLevel.MEDIUM, new RiskThreshold(UUID.randomUUID(), RiskLevel.MEDIUM, 21, 40, "retry"));
        thresholds.put(RiskLevel.HIGH, new RiskThreshold(UUID.randomUUID(), RiskLevel.HIGH, 41, 70, "require_approval"));
        thresholds.put(RiskLevel.CRITICAL, new RiskThreshold(UUID.randomUUID(), RiskLevel.CRITICAL, 71, 100, "block"));
        configStore.put("enabled", true);
        log.info("Configuration reloaded to defaults");
    }

    @Override
    public boolean isFeatureEnabled(String feature) {
        Object value = configStore.get(feature);
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof String) return "true".equalsIgnoreCase((String) value);
        return false;
    }

    @Override
    public void setThreshold(RiskLevel level, double minScore, double maxScore, String action) {
        thresholds.put(level, new RiskThreshold(UUID.randomUUID(), level, minScore, maxScore, action));
        log.info("Threshold set for {}: {}-{} -> {}", level, minScore, maxScore, action);
    }

    @Override
    public RiskThreshold getThreshold(RiskLevel level) {
        return thresholds.get(level);
    }

    public List<RiskThreshold> getThresholds() {
        return List.copyOf(thresholds.values());
    }
}
