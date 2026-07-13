package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalConfigurationService;
import com.sporekart.ai.approval.domain.DomainConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ApprovalConfigurationServiceImpl implements ApprovalConfigurationService {

    private final ConcurrentHashMap<String, DomainConfig> configStore = new ConcurrentHashMap<>();
    private final AtomicInteger versionCounter = new AtomicInteger(0);

    @Override
    public Optional<String> getConfig(String key) {
        return Optional.ofNullable(configStore.get(key))
            .map(DomainConfig::value);
    }

    @Override
    public void setConfig(String key, String value, String description) {
        var config = new DomainConfig(
            UUID.randomUUID(), key, value, description, true,
            versionCounter.incrementAndGet(), OffsetDateTime.now(), OffsetDateTime.now()
        );
        configStore.put(key, config);
        log.info("Configuration set: {} = {}", key, value);
    }

    @Override
    public List<DomainConfig> getAllConfigs() {
        return List.copyOf(configStore.values());
    }

    @Override
    public void reloadConfig() {
        configStore.clear();
        log.info("Configuration reloaded");
    }

    @Override
    public boolean isFeatureEnabled(String feature) {
        return Optional.ofNullable(configStore.get(feature))
            .map(c -> "true".equalsIgnoreCase(c.value()))
            .orElse(false);
    }
}
