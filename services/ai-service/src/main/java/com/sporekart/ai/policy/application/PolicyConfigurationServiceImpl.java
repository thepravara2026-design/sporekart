package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyConfigurationService;
import com.sporekart.ai.policy.domain.PolicyConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PolicyConfigurationServiceImpl implements PolicyConfigurationService {

    private final Map<String, PolicyConfiguration> configStore = new ConcurrentHashMap<>();

    @Override
    public Optional<String> getConfig(String key) {
        return Optional.ofNullable(configStore.get(key)).map(PolicyConfiguration::value);
    }

    @Override
    public void setConfig(String key, String value, String description) {
        PolicyConfiguration config = new PolicyConfiguration(
            UUID.randomUUID(), key, value, description, true, 1,
            OffsetDateTime.now(), OffsetDateTime.now()
        );
        configStore.put(key, config);
        log.info("Policy config set: {} = {}", key, value);
    }

    @Override
    public List<PolicyConfiguration> getAllConfigs() {
        return List.copyOf(configStore.values());
    }

    @Override
    public void reloadConfig() {
        log.info("Policy configuration reloaded (stub)");
    }

    @Override
    public boolean isFeatureEnabled(String feature) {
        return getConfig("feature." + feature).map("true"::equals).orElse(true);
    }
}
