package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.DecisionConfigurationService;
import com.sporekart.ai.decision.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class DecisionConfigurationServiceImpl implements DecisionConfigurationService {
    private final Map<String, DecisionConfig> configStore = new ConcurrentHashMap<>();

    @Override public Optional<String> getConfig(String key) {
        return Optional.ofNullable(configStore.get(key)).map(DecisionConfig::value);
    }
    @Override public void setConfig(String key, String value, String description) {
        configStore.put(key, new DecisionConfig(UUID.randomUUID(), key, value, description, true, 1, OffsetDateTime.now(), OffsetDateTime.now()));
        log.info("Decision config set: {} = {}", key, value);
    }
    @Override public List<DecisionConfig> getAllConfigs() { return List.copyOf(configStore.values()); }
    @Override public void reloadConfig() { log.info("Decision configuration reloaded (stub)"); }
    @Override public boolean isFeatureEnabled(String feature) {
        return getConfig("feature." + feature).map("true"::equals).orElse(true);
    }
}
