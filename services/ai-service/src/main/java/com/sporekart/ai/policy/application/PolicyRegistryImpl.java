package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyRegistry;
import com.sporekart.ai.policy.domain.*;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyRegistryEntity;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyRegistryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyRegistryImpl implements PolicyRegistry {

    private final PolicyRegistryRepository repository;

    @Override
    public com.sporekart.ai.policy.domain.PolicyRegistry register(com.sporekart.ai.policy.domain.PolicyRegistry registry) {
        PolicyRegistryEntity entity = new PolicyRegistryEntity();
        entity.setId(registry.id());
        entity.setName(registry.name());
        entity.setModule(registry.module());
        entity.setType(registry.type().name());
        entity.setScope(registry.scope().name());
        entity.setIsActive(registry.isActive());
        entity.setIsRegistered(true);
        entity.setConfig(registry.config() != null ? registry.config().toString() : "{}");
        entity.setRegisteredAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        repository.save(entity);
        return registry;
    }

    @Override
    public void unregister(UUID id) {
        repository.findById(id).ifPresent(e -> { e.setIsDeleted(true); repository.save(e); });
    }

    @Override
    public Optional<com.sporekart.ai.policy.domain.PolicyRegistry> findById(UUID id) {
        return repository.findByIdAndIsDeletedFalse(id).map(this::toDomain);
    }

    @Override
    public List<com.sporekart.ai.policy.domain.PolicyRegistry> findByModule(String module) {
        return repository.findByModuleAndIsDeletedFalse(module).stream().map(this::toDomain).toList();
    }

    @Override
    public List<com.sporekart.ai.policy.domain.PolicyRegistry> findByScope(PolicyScope scope) {
        return repository.findByScopeAndIsDeletedFalse(scope.name()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<com.sporekart.ai.policy.domain.PolicyRegistry> findAll() {
        return repository.findByIsDeletedFalse().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean isRegistered(String module, PolicyType type) {
        return repository.findByModuleAndTypeAndIsDeletedFalse(module, type.name()).isPresent();
    }

    private com.sporekart.ai.policy.domain.PolicyRegistry toDomain(PolicyRegistryEntity e) {
        return new com.sporekart.ai.policy.domain.PolicyRegistry(
            e.getId(), e.getName(), e.getModule(),
            PolicyType.valueOf(e.getType()), PolicyScope.valueOf(e.getScope()),
            e.getIsActive(), e.getIsRegistered(), new HashMap<>(),
            e.getRegisteredAt(), e.getUpdatedAt()
        );
    }
}
