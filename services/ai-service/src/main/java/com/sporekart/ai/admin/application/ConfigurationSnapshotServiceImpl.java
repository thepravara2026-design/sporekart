package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.api.ConfigurationSnapshotService;
import com.sporekart.ai.admin.domain.AdminConfiguration;
import com.sporekart.ai.admin.domain.ConfigurationSnapshot;
import com.sporekart.ai.admin.domain.ConfigurationStatus;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationEntity;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationRepository;
import com.sporekart.ai.admin.infrastructure.persistence.ConfigurationSnapshotEntity;
import com.sporekart.ai.admin.infrastructure.persistence.ConfigurationSnapshotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationSnapshotServiceImpl implements ConfigurationSnapshotService {

    private final ConfigurationSnapshotRepository configurationSnapshotRepository;
    private final AdminConfigurationRepository adminConfigurationRepository;
    private final AdministrationAuditService administrationAuditService;
    private final ObjectMapper objectMapper;

    @Override
    public ConfigurationSnapshot createSnapshot(String name, String environment, String description, UUID capturedBy) {
        var configs = adminConfigurationRepository.findByEnvironment(environment);
        var configMap = new HashMap<String, Object>();
        for (var config : configs) {
            configMap.put(config.getKey(), config.getValue());
        }
        var entity = new ConfigurationSnapshotEntity();
        entity.setName(name);
        try {
            entity.setConfiguration(objectMapper.writeValueAsString(configMap));
        } catch (JsonProcessingException e) {
            entity.setConfiguration("{}");
        }
        entity.setEnvironment(environment);
        entity.setDescription(description != null ? description : "Snapshot of " + environment);
        entity.setCapturedAt(LocalDateTime.now());
        entity.setCapturedBy(capturedBy);
        var saved = configurationSnapshotRepository.save(entity);
        administrationAuditService.recordAudit("SNAPSHOT_CREATE", "ConfigurationSnapshot", saved.getId(), capturedBy,
                Map.of("name", name, "environment", environment, "configCount", configMap.size()), null);
        log.info("Configuration snapshot '{}' created for environment {} with {} configs", name, environment, configMap.size());
        return toDomain(saved);
    }

    @Override
    public ConfigurationSnapshot getSnapshot(UUID id) {
        return configurationSnapshotRepository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<ConfigurationSnapshot> getSnapshotsByEnvironment(String environment) {
        return configurationSnapshotRepository.findByEnvironment(environment).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public AdminConfiguration restoreSnapshot(UUID snapshotId, UUID restoredBy) {
        var snapshotEntity = configurationSnapshotRepository.findById(snapshotId)
                .orElseThrow(() -> new IllegalArgumentException("Snapshot not found: " + snapshotId));
        Map<String, Object> configMap;
        try {
            configMap = objectMapper.readValue(snapshotEntity.getConfiguration(), Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse snapshot configuration", e);
        }
        AdminConfiguration lastConfig = null;
        for (var entry : configMap.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue() != null ? entry.getValue().toString() : "";
            var existing = adminConfigurationRepository.findByKeyAndModuleAndEnvironment(key, "*", snapshotEntity.getEnvironment());
            AdminConfigurationEntity entity;
            if (existing.isPresent()) {
                entity = existing.get();
                entity.setValue(value);
                entity.setVersion(entity.getVersion() + 1);
                entity.setUpdatedBy(restoredBy);
                entity.setUpdatedAt(LocalDateTime.now());
            } else {
                entity = new AdminConfigurationEntity();
                entity.setKey(key);
                entity.setValue(value);
                entity.setModule("*");
                entity.setEnvironment(snapshotEntity.getEnvironment());
                entity.setDescription("Restored from snapshot " + snapshotEntity.getName());
                entity.setStatus(ConfigurationStatus.ACTIVE.name());
                entity.setVersion(1);
                entity.setUpdatedBy(restoredBy);
            }
            var saved = adminConfigurationRepository.save(entity);
            lastConfig = new AdminConfiguration(
                    saved.getId(), saved.getKey(), saved.getValue(), saved.getModule(), saved.getEnvironment(),
                    saved.getDescription(), ConfigurationStatus.ACTIVE, saved.getVersion(),
                    saved.getUpdatedBy(), saved.getCreatedAt().toInstant(ZoneOffset.UTC),
                    saved.getUpdatedAt().toInstant(ZoneOffset.UTC)
            );
        }
        administrationAuditService.recordAudit("SNAPSHOT_RESTORE", "ConfigurationSnapshot", snapshotId, restoredBy,
                Map.of("name", snapshotEntity.getName(), "environment", snapshotEntity.getEnvironment(), "configCount", configMap.size()), null);
        log.info("Configuration snapshot '{}' restored for environment {}", snapshotEntity.getName(), snapshotEntity.getEnvironment());
        return lastConfig;
    }

    @SuppressWarnings("unchecked")
    private ConfigurationSnapshot toDomain(ConfigurationSnapshotEntity entity) {
        Map<String, Object> config = Map.of();
        if (entity.getConfiguration() != null) {
            try {
                config = objectMapper.readValue(entity.getConfiguration(), Map.class);
            } catch (Exception e) {
                // ignore
            }
        }
        return new ConfigurationSnapshot(
                entity.getId(),
                entity.getName(),
                config,
                entity.getEnvironment(),
                entity.getDescription(),
                entity.getCapturedAt() != null ? entity.getCapturedAt().toInstant(ZoneOffset.UTC) : Instant.now(),
                entity.getCapturedBy()
        );
    }
}
