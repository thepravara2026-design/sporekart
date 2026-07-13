package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.GovernanceLifecycleManager;
import com.sporekart.ai.governance.domain.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class GovernanceLifecycleManagerImpl implements GovernanceLifecycleManager {

    private final Map<UUID, List<GovernanceLifecycle>> lifecycleStore = new HashMap<>();

    @Override
    public GovernanceLifecycle transition(GovernancePolicy policy, String newStatus, String triggeredBy) {
        GovernanceLifecycle event = new GovernanceLifecycle(
            UUID.randomUUID(), policy.id(), "transition",
            policy.status().name(), newStatus, triggeredBy,
            Map.of("policyName", policy.name()), OffsetDateTime.now()
        );
        lifecycleStore.computeIfAbsent(policy.id(), k -> new ArrayList<>()).add(event);
        return event;
    }

    @Override
    public List<GovernanceLifecycle> getHistory(UUID policyId) {
        return lifecycleStore.getOrDefault(policyId, List.of());
    }

    @Override
    public boolean canTransition(String fromStatus, String toStatus) {
        Set<String> validTransitions = Set.of(
            "DRAFT->ACTIVE", "ACTIVE->INACTIVE", "INACTIVE->ACTIVE",
            "ACTIVE->ARCHIVED", "INACTIVE->ARCHIVED", "ARCHIVED->DEPRECATED"
        );
        return validTransitions.contains(fromStatus + "->" + toStatus);
    }
}
