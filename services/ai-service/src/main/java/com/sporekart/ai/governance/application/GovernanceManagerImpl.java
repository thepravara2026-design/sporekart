package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.GovernanceManager;
import com.sporekart.ai.governance.domain.*;
import com.sporekart.ai.governance.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GovernanceManagerImpl implements GovernanceManager {

    private final GovernanceRepository policyRepository;
    private final GovernanceConfigurationRepository configRepository;

    @Override
    public GovernancePolicy createPolicy(GovernancePolicy policy) {
        GovernanceEntity entity = toEntity(policy);
        policyRepository.save(entity);
        return policy;
    }

    @Override
    public GovernancePolicy updatePolicy(GovernancePolicy policy) {
        GovernanceEntity entity = toEntity(policy);
        entity.setUpdatedAt(OffsetDateTime.now());
        policyRepository.save(entity);
        return policy;
    }

    @Override
    public void deletePolicy(UUID id) {
        policyRepository.findById(id).ifPresent(e -> { e.setIsDeleted(true); policyRepository.save(e); });
    }

    @Override
    public Optional<GovernancePolicy> getPolicy(UUID id) {
        return policyRepository.findByIdAndIsDeletedFalse(id).map(this::toDomain);
    }

    @Override
    public List<GovernancePolicy> listPolicies() {
        return policyRepository.findByIsDeletedFalse().stream().map(this::toDomain).toList();
    }

    @Override
    public List<GovernancePolicy> listPoliciesByScope(GovernanceScope scope) {
        return policyRepository.findByScopeAndIsDeletedFalse(scope.name()).stream().map(this::toDomain).toList();
    }

    @Override
    public GovernanceConfiguration setConfiguration(GovernanceConfiguration config) {
        GovernanceConfigurationEntity entity = new GovernanceConfigurationEntity();
        entity.setId(config.id());
        entity.setConfigKey(config.key());
        entity.setConfigValue(config.value());
        entity.setDescription(config.description());
        entity.setScope(config.scope().name());
        entity.setMode(config.mode().name());
        entity.setMetadata(String.valueOf(config.metadata()));
        entity.setIsActive(config.isActive());
        entity.setVersion(config.version());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        configRepository.save(entity);
        log.info("Configuration set: {} = {}", config.key(), config.value());
        return config;
    }

    @Override
    public GovernanceConfiguration getConfiguration(String key) {
        return configRepository.findByConfigKeyAndIsDeletedFalse(key)
            .map(e -> new GovernanceConfiguration(
                e.getId(), e.getConfigKey(), e.getConfigValue(), e.getDescription(),
                GovernanceScope.valueOf(e.getScope()), GovernanceMode.valueOf(e.getMode()),
                Map.of(), e.getIsActive(), e.getVersion(), e.getCreatedAt(), e.getUpdatedAt()
            )).orElse(null);
    }

    @Override
    public List<GovernanceConfiguration> getAllConfigurations() {
        return configRepository.findByIsDeletedFalse().stream()
            .map(e -> new GovernanceConfiguration(
                e.getId(), e.getConfigKey(), e.getConfigValue(), e.getDescription(),
                GovernanceScope.valueOf(e.getScope()), GovernanceMode.valueOf(e.getMode()),
                Map.of(), e.getIsActive(), e.getVersion(), e.getCreatedAt(), e.getUpdatedAt()
            )).toList();
    }

    @Override
    public void reloadConfiguration() {
        log.info("Configuration reloaded (stub)");
    }

    private GovernanceEntity toEntity(GovernancePolicy p) {
        GovernanceEntity e = new GovernanceEntity();
        e.setId(p.id());
        e.setName(p.name());
        e.setDescription(p.description());
        e.setScope(p.scope().name());
        e.setStatus(p.status().name());
        e.setPriority(p.priority());
        e.setRules(String.valueOf(p.rules()));
        e.setConditions(String.valueOf(p.conditions()));
        e.setIsActive(p.isActive());
        e.setCreatedBy(p.createdBy());
        e.setCreatedAt(OffsetDateTime.now());
        e.setUpdatedAt(OffsetDateTime.now());
        return e;
    }

    @SuppressWarnings("unchecked")
    private GovernancePolicy toDomain(GovernanceEntity e) {
        return new GovernancePolicy(
            e.getId(), e.getName(), e.getDescription(),
            GovernanceScope.valueOf(e.getScope()), GovernanceStatus.valueOf(e.getStatus()),
            e.getPriority(), new HashMap<>(), new HashMap<>(),
            e.getIsActive(), e.getCreatedBy(), e.getCreatedAt(), e.getUpdatedAt()
        );
    }
}
