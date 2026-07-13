package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyResolver;
import com.sporekart.ai.policy.domain.*;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyEntity;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PolicyResolverImpl implements PolicyResolver {

    private final PolicyRepository repository;

    @Override
    public List<Policy> resolvePolicies(EvaluationRequest request) {
        return repository.findByIsDeletedFalse().stream()
            .filter(PolicyEntity::getIsActive)
            .map(this::toDomain)
            .filter(p -> p.status() == PolicyStatus.ACTIVE)
            .sorted(Comparator.comparingInt(Policy::priority).reversed())
            .toList();
    }

    @Override
    public List<Policy> resolvePoliciesByModule(String module) {
        return repository.findByModuleAndIsDeletedFalse(module).stream()
            .filter(PolicyEntity::getIsActive)
            .map(this::toDomain)
            .toList();
    }

    @Override
    public List<Policy> resolvePoliciesByScope(PolicyScope scope) {
        return repository.findByScopeAndIsDeletedFalse(scope.name()).stream()
            .filter(PolicyEntity::getIsActive)
            .map(this::toDomain)
            .toList();
    }

    @Override
    public List<Policy> resolveActivePolicies() {
        return repository.findByStatusAndIsDeletedFalse("ACTIVE").stream()
            .map(this::toDomain)
            .toList();
    }

    @Override
    public Policy resolvePolicy(UUID id) {
        return repository.findByIdAndIsDeletedFalse(id).map(this::toDomain).orElse(null);
    }

    private Policy toDomain(PolicyEntity e) {
        return new Policy(
            e.getId(), e.getName(), e.getDescription(),
            PolicyType.valueOf(e.getType()), PolicyStatus.valueOf(e.getStatus()),
            PolicySeverity.valueOf(e.getSeverity()), PolicyScope.valueOf(e.getScope()),
            e.getPriority(), e.getModule(),
            new ArrayList<>(), new ArrayList<>(),
            new HashMap<>(), e.getIsActive(), e.getIsSystem(),
            e.getCreatedBy(), e.getCreatedAt(), e.getUpdatedAt()
        );
    }
}
