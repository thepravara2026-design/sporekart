package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.DecisionAuditService;
import com.sporekart.ai.decision.domain.*;
import com.sporekart.ai.decision.infrastructure.persistence.DecisionAuditEntity;
import com.sporekart.ai.decision.infrastructure.persistence.DecisionAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DecisionAuditServiceImpl implements DecisionAuditService {
    private final DecisionAuditRepository repository;

    @Override
    public DecisionAudit recordAudit(DecisionAudit audit) {
        DecisionAuditEntity entity = new DecisionAuditEntity();
        entity.setId(audit.id()); entity.setRequestId(audit.requestId());
        entity.setDecisionId(audit.decisionId());
        entity.setAction(audit.action().name()); entity.setStatus(audit.status().name());
        entity.setConfidence(audit.confidence().name());
        entity.setReasons(audit.reasons() != null ? audit.reasons().toString() : "[]");
        entity.setContext(audit.context() != null ? audit.context().toString() : "{}");
        entity.setUserId(audit.userId());
        entity.setProcessingTimeMs(audit.processingTimeMs());
        entity.setSuccess(audit.success()); entity.setTimestamp(audit.timestamp());
        entity.setCreatedAt(OffsetDateTime.now());
        repository.save(entity);
        return audit;
    }

    @Override public List<DecisionAudit> findByRequestId(UUID requestId) {
        return repository.findByRequestIdAndIsDeletedFalse(requestId).stream().map(this::toDomain).toList();
    }
    @Override public List<DecisionAudit> findByUserId(String userId) {
        return repository.findByUserIdAndIsDeletedFalse(userId).stream().map(this::toDomain).toList();
    }
    @Override public List<DecisionAudit> findByAction(DecisionAction action) {
        return repository.findByActionAndIsDeletedFalse(action.name()).stream().map(this::toDomain).toList();
    }
    @Override public List<DecisionAudit> findByStatus(DecisionStatus status) {
        return repository.findByStatusAndIsDeletedFalse(status.name()).stream().map(this::toDomain).toList();
    }
    @Override public List<DecisionAudit> findByDateRange(OffsetDateTime start, OffsetDateTime end) {
        return repository.findByTimestampBetweenAndIsDeletedFalse(start, end).stream().map(this::toDomain).toList();
    }

    private DecisionAudit toDomain(DecisionAuditEntity e) {
        return new DecisionAudit(e.getId(), e.getRequestId(), e.getDecisionId(),
            DecisionAction.valueOf(e.getAction()), DecisionStatus.valueOf(e.getStatus()),
            DecisionConfidence.valueOf(e.getConfidence()), new ArrayList<>(),
            new HashMap<>(), e.getUserId(), e.getProcessingTimeMs(),
            e.getSuccess(), e.getTimestamp(), e.getCreatedAt());
    }
}
