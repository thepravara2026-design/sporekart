package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.api.EnvironmentManager;
import com.sporekart.ai.admin.domain.EnvironmentProfile;
import com.sporekart.ai.admin.domain.EnvironmentType;
import com.sporekart.ai.admin.infrastructure.persistence.EnvironmentProfileEntity;
import com.sporekart.ai.admin.infrastructure.persistence.EnvironmentProfileRepository;
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
public class EnvironmentManagerImpl implements EnvironmentManager {

    private final EnvironmentProfileRepository environmentProfileRepository;
    private final AdministrationAuditService administrationAuditService;

    @Override
    public EnvironmentProfile getEnvironment(String name) {
        return environmentProfileRepository.findByName(name)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public EnvironmentProfile createEnvironment(EnvironmentProfile profile) {
        var entity = new EnvironmentProfileEntity();
        entity.setName(profile.name());
        entity.setType(profile.type() != null ? profile.type().name() : null);
        entity.setDescription(profile.description());
        entity.setActive(profile.active());
        entity.setConfigSource(profile.configSource());
        var saved = environmentProfileRepository.save(entity);
        administrationAuditService.recordAudit("ENVIRONMENT_CREATE", "EnvironmentProfile", saved.getId(), null,
                Map.of("name", profile.name(), "type", profile.type() != null ? profile.type().name() : null), null);
        log.info("Environment {} created", profile.name());
        return toDomain(saved);
    }

    @Override
    public EnvironmentProfile updateEnvironment(UUID id, EnvironmentProfile profile) {
        var entity = environmentProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Environment not found: " + id));
        entity.setName(profile.name());
        entity.setType(profile.type() != null ? profile.type().name() : entity.getType());
        entity.setDescription(profile.description() != null ? profile.description() : entity.getDescription());
        entity.setActive(profile.active());
        entity.setConfigSource(profile.configSource() != null ? profile.configSource() : entity.getConfigSource());
        entity.setUpdatedAt(LocalDateTime.now());
        var saved = environmentProfileRepository.save(entity);
        administrationAuditService.recordAudit("ENVIRONMENT_UPDATE", "EnvironmentProfile", saved.getId(), null,
                Map.of("name", profile.name()), null);
        log.info("Environment {} updated", profile.name());
        return toDomain(saved);
    }

    @Override
    public List<EnvironmentProfile> getAllEnvironments() {
        return environmentProfileRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public EnvironmentProfile switchEnvironment(String name) {
        var all = environmentProfileRepository.findAll();
        for (var env : all) {
            env.setActive(false);
            env.setUpdatedAt(LocalDateTime.now());
            environmentProfileRepository.save(env);
        }
        var target = environmentProfileRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Environment not found: " + name));
        target.setActive(true);
        target.setUpdatedAt(LocalDateTime.now());
        var saved = environmentProfileRepository.save(target);
        administrationAuditService.recordAudit("ENVIRONMENT_SWITCH", "EnvironmentProfile", saved.getId(), null,
                Map.of("name", name), null);
        log.info("Switched to environment {}", name);
        return toDomain(saved);
    }

    private EnvironmentProfile toDomain(EnvironmentProfileEntity entity) {
        return new EnvironmentProfile(
                entity.getId(),
                entity.getName(),
                entity.getType() != null ? EnvironmentType.valueOf(entity.getType()) : null,
                entity.getDescription(),
                entity.isActive(),
                entity.getConfigSource(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toInstant(ZoneOffset.UTC) : Instant.now(),
                entity.getUpdatedAt() != null ? entity.getUpdatedAt().toInstant(ZoneOffset.UTC) : Instant.now()
        );
    }
}
