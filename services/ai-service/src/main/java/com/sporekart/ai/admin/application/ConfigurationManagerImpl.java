package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.api.AdministrationMetricsService;
import com.sporekart.ai.admin.api.ConfigurationManager;
import com.sporekart.ai.admin.api.ConfigurationVersionManager;
import com.sporekart.ai.admin.domain.AdminConfiguration;
import com.sporekart.ai.admin.domain.ConfigurationStatus;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationEntity;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationRepository;
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
public class ConfigurationManagerImpl implements ConfigurationManager {

    private final AdminConfigurationRepository adminConfigurationRepository;
    private final ConfigurationVersionManager configurationVersionManager;
    private final AdministrationAuditService administrationAuditService;
    private final AdministrationMetricsService administrationMetricsService;

    @Override
    public AdminConfiguration getConfiguration(String key, String module, String environment) {
        return adminConfigurationRepository.findByKeyAndModuleAndEnvironment(key, module, environment)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public AdminConfiguration setConfiguration(String key, String value, String module, String environment, String description, UUID updatedBy) {
        var existing = adminConfigurationRepository.findByKeyAndModuleAndEnvironment(key, module, environment);
        AdminConfigurationEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            configurationVersionManager.createVersion(entity.getId(), entity.getValue(), "Updated by " + updatedBy, updatedBy);
            entity.setValue(value);
            entity.setDescription(description != null ? description : entity.getDescription());
            entity.setVersion(entity.getVersion() + 1);
            entity.setUpdatedBy(updatedBy);
            entity.setStatus(ConfigurationStatus.ACTIVE.name());
            entity.setUpdatedAt(LocalDateTime.now());
            adminConfigurationRepository.save(entity);
            administrationAuditService.recordAudit("CONFIG_UPDATE", "AdminConfiguration", entity.getId(), updatedBy,
                    Map.of("key", key, "module", module, "environment", environment), null);
            administrationMetricsService.getStatistics();
            log.info("Configuration {} updated for {}/{}", key, module, environment);
        } else {
            entity = new AdminConfigurationEntity();
            entity.setKey(key);
            entity.setValue(value);
            entity.setModule(module);
            entity.setEnvironment(environment);
            entity.setDescription(description);
            entity.setStatus(ConfigurationStatus.ACTIVE.name());
            entity.setVersion(1);
            entity.setUpdatedBy(updatedBy);
            adminConfigurationRepository.save(entity);
            configurationVersionManager.createVersion(entity.getId(), value, "Created by " + updatedBy, updatedBy);
            administrationAuditService.recordAudit("CONFIG_CREATE", "AdminConfiguration", entity.getId(), updatedBy,
                    Map.of("key", key, "module", module, "environment", environment), null);
            administrationMetricsService.getStatistics();
            log.info("Configuration {} created for {}/{}", key, module, environment);
        }
        return toDomain(entity);
    }

    @Override
    public List<AdminConfiguration> getAllConfigurations(String module) {
        return adminConfigurationRepository.findByModule(module).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public AdminConfiguration deleteConfiguration(UUID id) {
        var entity = adminConfigurationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + id));
        entity.setStatus(ConfigurationStatus.INACTIVE.name());
        entity.setUpdatedAt(LocalDateTime.now());
        var saved = adminConfigurationRepository.save(entity);
        administrationAuditService.recordAudit("CONFIG_DELETE", "AdminConfiguration", saved.getId(), saved.getUpdatedBy(),
                Map.of("key", saved.getKey(), "module", saved.getModule(), "environment", saved.getEnvironment()), null);
        log.info("Configuration {} soft-deleted", id);
        return toDomain(saved);
    }

    @Override
    public List<AdminConfiguration> getConfigurationsByEnvironment(String environment) {
        return adminConfigurationRepository.findByEnvironment(environment).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public AdminConfiguration validateConfiguration(String key, String value) {
        if (key == null || key.isBlank() || value == null || value.isBlank()) {
            throw new IllegalArgumentException("Key and value must not be empty");
        }
        return new AdminConfiguration(null, key, value, null, null, null,
                ConfigurationStatus.ACTIVE, 0, null, Instant.now(), Instant.now());
    }

    private AdminConfiguration toDomain(AdminConfigurationEntity entity) {
        return new AdminConfiguration(
                entity.getId(),
                entity.getKey(),
                entity.getValue(),
                entity.getModule(),
                entity.getEnvironment(),
                entity.getDescription(),
                entity.getStatus() != null ? ConfigurationStatus.valueOf(entity.getStatus()) : ConfigurationStatus.ACTIVE,
                entity.getVersion(),
                entity.getUpdatedBy(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toInstant(ZoneOffset.UTC) : Instant.now(),
                entity.getUpdatedAt() != null ? entity.getUpdatedAt().toInstant(ZoneOffset.UTC) : Instant.now()
        );
    }
}
