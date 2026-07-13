package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.DecisionResolver;
import com.sporekart.ai.decision.domain.*;
import com.sporekart.ai.decision.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DecisionResolverImpl implements DecisionResolver {
    private final DecisionRepository decisionRepository;
    private final DecisionRuleRepository ruleRepository;
    private final DecisionRegistryRepository registryRepository;

    @Override
    public DecisionContext resolveContext(DecisionRequest request) {
        return new DecisionContext(
            UUID.randomUUID(), request.id(), request.module(), request.action(),
            request.payload() != null ? request.payload() : new HashMap<>(),
            Map.of("userId", request.userId(), "roles", request.roles()),
            Map.of("timestamp", OffsetDateTime.now().toString()),
            request.roles(), request.matchedPolicyIds(),
            Map.of("policies", request.matchedPolicyIds(), "rules", request.matchedRuleIds()),
            OffsetDateTime.now()
        );
    }

    @Override
    public List<DecisionRule> resolveRules(DecisionRequest request) {
        return ruleRepository.findByIsDeletedFalse().stream()
            .map(e -> new DecisionRule(
                e.getId(), e.getName(), e.getDescription(),
                e.getAction() != null ? DecisionAction.valueOf(e.getAction()) : DecisionAction.ALLOW,
                e.getPriority(), e.getWeight(), new HashMap<>(), new HashMap<>(),
                e.getIsActive(), e.getCreatedAt(), e.getUpdatedAt()
            )).toList();
    }

    @Override
    public List<DecisionRegistry> resolveRegistries(String module) {
        return registryRepository.findByModuleAndIsDeletedFalse(module).stream()
            .map(e -> new DecisionRegistry(
                e.getId(), e.getName(), e.getModule(), e.getEndpoint(),
                e.getIsActive(), e.getIsRegistered(), new HashMap<>(),
                e.getRegisteredAt(), e.getUpdatedAt()
            )).toList();
    }

    @Override
    public Optional<DecisionResult> findById(UUID id) {
        return decisionRepository.findByIdAndIsDeletedFalse(id)
            .map(e -> new DecisionResult(
                e.getId(), e.getRequestId(),
                DecisionAction.valueOf(e.getAction()),
                DecisionStatus.valueOf(e.getStatus()),
                DecisionConfidence.valueOf(e.getConfidence()),
                e.getSummary(), List.of(), List.of(), null,
                e.getProcessingTimeMs(), e.getRequiresApproval(),
                e.getOverrideable(), e.getTimestamp()
            ));
    }

    @Override
    public List<DecisionResult> findByRequestId(UUID requestId) {
        return decisionRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .map(e -> new DecisionResult(
                e.getId(), e.getRequestId(),
                DecisionAction.valueOf(e.getAction()),
                DecisionStatus.valueOf(e.getStatus()),
                DecisionConfidence.valueOf(e.getConfidence()),
                e.getSummary(), List.of(), List.of(), null,
                e.getProcessingTimeMs(), e.getRequiresApproval(),
                e.getOverrideable(), e.getTimestamp()
            )).toList();
    }
}
