package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.api.FeatureFlagService;
import com.sporekart.ai.admin.domain.FeatureFlag;
import com.sporekart.ai.admin.infrastructure.persistence.FeatureFlagEntity;
import com.sporekart.ai.admin.infrastructure.persistence.FeatureFlagRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class FeatureFlagServiceImpl implements FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;
    private final AdministrationAuditService administrationAuditService;
    private final ObjectMapper objectMapper;

    @Override
    public FeatureFlag getFeatureFlag(String key) {
        return featureFlagRepository.findByKey(key)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public FeatureFlag setFeatureFlag(String key, boolean enabled, String environment, String module, Map<String, Object> metadata, UUID updatedBy) {
        var existing = featureFlagRepository.findByKey(key);
        FeatureFlagEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            boolean previousEnabled = entity.isEnabled();
            entity.setEnabled(enabled);
            entity.setEnvironment(environment != null ? environment : entity.getEnvironment());
            entity.setModule(module != null ? module : entity.getModule());
            try {
                entity.setMetadata(objectMapper.writeValueAsString(metadata));
            } catch (JsonProcessingException e) {
                // ignore
            }
            entity.setUpdatedAt(LocalDateTime.now());
            featureFlagRepository.save(entity);
            administrationAuditService.recordAudit("FEATURE_FLAG_CHANGE", "FeatureFlag", entity.getId(), updatedBy,
                    Map.of("key", key, "enabled", enabled, "previousEnabled", previousEnabled), null);
            log.info("Feature flag {} updated: enabled={}", key, enabled);
        } else {
            entity = new FeatureFlagEntity();
            entity.setKey(key);
            entity.setName(key);
            entity.setEnabled(enabled);
            entity.setEnvironment(environment);
            entity.setModule(module);
            try {
                entity.setMetadata(objectMapper.writeValueAsString(metadata));
            } catch (JsonProcessingException e) {
                // ignore
            }
            featureFlagRepository.save(entity);
            administrationAuditService.recordAudit("FEATURE_FLAG_CREATE", "FeatureFlag", entity.getId(), updatedBy,
                    Map.of("key", key, "enabled", enabled), null);
            log.info("Feature flag {} created: enabled={}", key, enabled);
        }
        return toDomain(entity);
    }

    @Override
    public List<FeatureFlag> getAllFeatureFlags() {
        return featureFlagRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<FeatureFlag> getFeatureFlagsByModule(String module) {
        return featureFlagRepository.findByModule(module).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<FeatureFlag> getFeatureFlagsByEnvironment(String environment) {
        return featureFlagRepository.findByEnvironment(environment).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean isFeatureEnabled(String key) {
        return featureFlagRepository.findByKey(key)
                .map(FeatureFlagEntity::isEnabled)
                .orElse(false);
    }

    @SuppressWarnings("unchecked")
    private FeatureFlag toDomain(FeatureFlagEntity entity) {
        Map<String, Object> metadata = Map.of();
        if (entity.getMetadata() != null) {
            try {
                metadata = objectMapper.readValue(entity.getMetadata(), Map.class);
            } catch (Exception e) {
                // ignore
            }
        }
        return new FeatureFlag(
                entity.getId(),
                entity.getKey(),
                entity.getName(),
                entity.getDescription(),
                entity.isEnabled(),
                entity.getEnvironment(),
                entity.getModule(),
                metadata,
                entity.getCreatedAt() != null ? entity.getCreatedAt().toInstant(ZoneOffset.UTC) : Instant.now(),
                entity.getUpdatedAt() != null ? entity.getUpdatedAt().toInstant(ZoneOffset.UTC) : Instant.now()
        );
    }
}
