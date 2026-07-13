package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.EscalationManager;
import com.sporekart.ai.automation.domain.Escalation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EscalationManagerImpl implements EscalationManager {

    private final ConcurrentHashMap<UUID, Escalation> escalations = new ConcurrentHashMap<>();

    @Override
    public void triggerEscalation(UUID entityId, String entityType, String reason) {
        var id = UUID.randomUUID();
        var escalation = new Escalation(
            id, entityId, entityType, reason, 1, false, Instant.now(), null
        );
        escalations.put(id, escalation);
        log.warn("Escalation triggered for {} {}: {}", entityType, entityId, reason);
    }

    @Override
    public List<Escalation> getActiveEscalations() {
        return escalations.values().stream()
            .filter(e -> !e.resolved())
            .collect(Collectors.toList());
    }

    @Override
    public void resolveEscalation(UUID id) {
        var existing = escalations.get(id);
        if (existing != null) {
            var resolved = new Escalation(
                existing.id(), existing.entityId(), existing.entityType(),
                existing.reason(), existing.level(), true,
                existing.createdAt(), Instant.now()
            );
            escalations.put(id, resolved);
            log.info("Resolved escalation: {}", id);
        }
    }
}
