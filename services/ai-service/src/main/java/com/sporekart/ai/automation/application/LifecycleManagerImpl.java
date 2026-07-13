package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.api.LifecycleManager;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.infrastructure.persistence.LifecycleDefinitionRepository;
import com.sporekart.ai.automation.infrastructure.persistence.LifecycleHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LifecycleManagerImpl implements LifecycleManager {

    private final LifecycleDefinitionRepository definitionRepository;
    private final LifecycleHistoryRepository historyRepository;
    private final AutomationAuditService auditService;

    @Override
    public LifecycleDefinition registerLifecycle(LifecycleDefinition definition) {
        var saved = definitionRepository.save(definition);
        auditService.recordAudit("LIFECYCLE_REGISTERED", "LifecycleDefinition", saved.id(), null,
            Map.of("name", saved.name(), "entityType", saved.entityType()), null);
        log.info("Registered lifecycle definition: {} for {}", saved.name(), saved.entityType());
        return saved;
    }

    @Override
    public LifecycleState getCurrentState(UUID entityId, String entityType) {
        var history = historyRepository.findByEntityIdAndEntityTypeOrderByEnteredAtAsc(entityId, entityType);
        if (history.isEmpty()) {
            return null;
        }
        return history.get(history.size() - 1);
    }

    @Override
    public LifecycleState transition(UUID entityId, String entityType, LifecycleStateType targetState, String triggeredBy, String reason) {
        var current = getCurrentState(entityId, entityType);
        var definitions = definitionRepository.findAll();
        var definition = definitions.stream()
            .filter(d -> d.entityType().equals(entityType))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No lifecycle definition found for entity type: " + entityType));

        var fromState = current != null ? current.currentState() : definition.initialState();
        var transitions = definition.transitions();
        if (transitions != null && transitions.containsKey(fromState)) {
            var allowedTransitions = transitions.get(fromState);
            if (!allowedTransitions.containsValue(targetState) && !allowedTransitions.containsKey(targetState.toString())) {
                throw new IllegalStateException("Transition from " + fromState + " to " + targetState + " is not allowed");
            }
        }

        var now = Instant.now();
        var state = new LifecycleState(
            UUID.randomUUID(), entityId, entityType, targetState,
            Map.of("triggeredBy", triggeredBy, "reason", reason, "fromState", fromState.toString()),
            now, now
        );
        var saved = historyRepository.save(state);
        auditService.recordAudit("LIFECYCLE_TRANSITION", entityType, entityId, null,
            Map.of("fromState", fromState.toString(), "toState", targetState.toString(), "reason", reason), null);
        log.info("Transitioned {} {} from {} to {} triggered by {}", entityType, entityId, fromState, targetState, triggeredBy);
        return saved;
    }

    @Override
    public List<LifecycleState> getHistory(UUID entityId, String entityType) {
        return historyRepository.findByEntityIdAndEntityTypeOrderByEnteredAtAsc(entityId, entityType);
    }

    @Override
    public List<LifecycleDefinition> getAllLifecycles() {
        return definitionRepository.findAll();
    }
}
