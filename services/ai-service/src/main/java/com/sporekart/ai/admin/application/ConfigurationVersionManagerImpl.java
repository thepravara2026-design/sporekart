package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.api.ConfigurationVersionManager;
import com.sporekart.ai.admin.domain.AdminConfiguration;
import com.sporekart.ai.admin.domain.ConfigurationStatus;
import com.sporekart.ai.admin.domain.ConfigurationVersion;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationEntity;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationRepository;
import com.sporekart.ai.admin.infrastructure.persistence.ConfigurationVersionEntity;
import com.sporekart.ai.admin.infrastructure.persistence.ConfigurationVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationVersionManagerImpl implements ConfigurationVersionManager {

    private final ConfigurationVersionRepository configurationVersionRepository;
    private final AdminConfigurationRepository adminConfigurationRepository;
    private final AdministrationAuditService administrationAuditService;

    @Override
    public ConfigurationVersion createVersion(UUID configId, String value, String changeReason, UUID changedBy) {
        var configEntity = adminConfigurationRepository.findById(configId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + configId));
        var entity = new ConfigurationVersionEntity();
        entity.setConfigId(configId);
        entity.setVersion(configEntity.getVersion());
        entity.setValue(value);
        entity.setChangeReason(changeReason);
        entity.setChangedBy(changedBy);
        entity.setChangedAt(LocalDateTime.now());
        var saved = configurationVersionRepository.save(entity);
        log.debug("Version {} created for config {}", saved.getVersion(), configId);
        return toDomain(saved);
    }

    @Override
    public List<ConfigurationVersion> getVersions(UUID configId) {
        return configurationVersionRepository.findByConfigIdOrderByVersionDesc(configId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public ConfigurationVersion getVersion(UUID configId, int version) {
        return configurationVersionRepository.findByConfigIdOrderByVersionDesc(configId).stream()
                .filter(v -> v.getVersion() == version)
                .findFirst()
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public AdminConfiguration rollback(UUID configId, int targetVersion, UUID rolledBackBy) {
        var target = configurationVersionRepository.findByConfigIdOrderByVersionDesc(configId).stream()
                .filter(v -> v.getVersion() == targetVersion)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Version " + targetVersion + " not found for config " + configId));
        var configEntity = adminConfigurationRepository.findById(configId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + configId));
        int newVersion = configEntity.getVersion() + 1;
        var versionEntity = new ConfigurationVersionEntity();
        versionEntity.setConfigId(configId);
        versionEntity.setVersion(newVersion);
        versionEntity.setValue(target.getValue());
        versionEntity.setChangeReason("Rollback to version " + targetVersion + " by " + rolledBackBy);
        versionEntity.setChangedBy(rolledBackBy);
        versionEntity.setChangedAt(LocalDateTime.now());
        configurationVersionRepository.save(versionEntity);
        configEntity.setValue(target.getValue());
        configEntity.setVersion(newVersion);
        configEntity.setUpdatedBy(rolledBackBy);
        configEntity.setUpdatedAt(LocalDateTime.now());
        var saved = adminConfigurationRepository.save(configEntity);
        administrationAuditService.recordAudit("CONFIG_ROLLBACK", "AdminConfiguration", configId, rolledBackBy,
                Map.of("targetVersion", targetVersion, "newVersion", newVersion, "key", configEntity.getKey()), null);
        log.info("Configuration {} rolled back to version {}", configId, targetVersion);
        return new AdminConfiguration(
                saved.getId(), saved.getKey(), saved.getValue(), saved.getModule(), saved.getEnvironment(),
                saved.getDescription(), ConfigurationStatus.ACTIVE, saved.getVersion(),
                saved.getUpdatedBy(), saved.getCreatedAt().toInstant(ZoneOffset.UTC),
                saved.getUpdatedAt().toInstant(ZoneOffset.UTC)
        );
    }

    private ConfigurationVersion toDomain(ConfigurationVersionEntity entity) {
        return new ConfigurationVersion(
                entity.getId(),
                entity.getConfigId(),
                entity.getVersion(),
                entity.getValue(),
                entity.getChangeReason(),
                entity.getChangedBy(),
                entity.getChangedAt() != null ? entity.getChangedAt().toInstant(ZoneOffset.UTC) : Instant.now()
        );
    }
}
