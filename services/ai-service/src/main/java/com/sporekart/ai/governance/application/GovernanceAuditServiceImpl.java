package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.GovernanceAuditService;
import com.sporekart.ai.governance.domain.*;
import com.sporekart.ai.governance.infrastructure.persistence.GovernanceAuditEntity;
import com.sporekart.ai.governance.infrastructure.persistence.GovernanceAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GovernanceAuditServiceImpl implements GovernanceAuditService {

    private final GovernanceAuditRepository repository;

    @Override
    public GovernanceAudit recordAudit(GovernanceAudit audit) {
        GovernanceAuditEntity entity = toEntity(audit);
        repository.save(entity);
        return audit;
    }

    @Override
    public List<GovernanceAudit> findByRequestId(UUID requestId) {
        return repository.findByRequestIdAndIsDeletedFalse(requestId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<GovernanceAudit> findByUserId(String userId) {
        return repository.findByUserIdAndIsDeletedFalse(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<GovernanceAudit> findByDateRange(OffsetDateTime start, OffsetDateTime end) {
        return repository.findByTimestampBetweenAndIsDeletedFalse(start, end).stream().map(this::toDomain).toList();
    }

    @Override
    public List<GovernanceAudit> findByDecision(GovernanceDecision decision) {
        return repository.findByDecisionAndIsDeletedFalse(decision.name()).stream().map(this::toDomain).toList();
    }

    private GovernanceAuditEntity toEntity(GovernanceAudit a) {
        GovernanceAuditEntity e = new GovernanceAuditEntity();
        e.setId(a.id());
        e.setRequestId(a.requestId());
        e.setAction(a.action());
        e.setModule(a.module());
        e.setDecision(a.decision().name());
        e.setViolations(a.violations() != null ? a.violations().toString() : "[]");
        e.setContext(a.context() != null ? a.context().toString() : "{}");
        e.setUserId(a.userId());
        e.setProcessingTimeMs(a.processingTimeMs());
        e.setSuccess(a.success());
        e.setTimestamp(a.timestamp());
        e.setCreatedAt(OffsetDateTime.now());
        return e;
    }

    private GovernanceAudit toDomain(GovernanceAuditEntity e) {
        return new GovernanceAudit(
            e.getId(), e.getRequestId(), e.getAction(), e.getModule(),
            GovernanceDecision.valueOf(e.getDecision()), new ArrayList<>(),
            new HashMap<>(), e.getUserId(), e.getProcessingTimeMs(),
            e.getSuccess(), e.getTimestamp(), e.getCreatedAt()
        );
    }
}
