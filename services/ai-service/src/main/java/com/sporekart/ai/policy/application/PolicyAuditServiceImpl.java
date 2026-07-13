package com.sporekart.ai.policy.application;

import com.sporekart.ai.policy.api.PolicyAuditService;
import com.sporekart.ai.policy.domain.*;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyAuditEntity;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PolicyAuditServiceImpl implements PolicyAuditService {

    private final PolicyAuditRepository repository;

    @Override
    public PolicyAudit recordAudit(PolicyAudit audit) {
        PolicyAuditEntity entity = new PolicyAuditEntity();
        entity.setId(audit.id());
        entity.setPolicyId(audit.policyId());
        entity.setRequestId(audit.requestId());
        entity.setAction(audit.action());
        entity.setDecision(audit.decision().name());
        entity.setViolations(audit.violations() != null ? audit.violations().toString() : "[]");
        entity.setDetails(audit.details() != null ? audit.details().toString() : "{}");
        entity.setUserId(audit.userId());
        entity.setProcessingTimeMs(audit.processingTimeMs());
        entity.setSuccess(audit.success());
        entity.setTimestamp(audit.timestamp());
        entity.setCreatedAt(OffsetDateTime.now());
        repository.save(entity);
        return audit;
    }

    @Override
    public List<PolicyAudit> findByPolicyId(UUID policyId) {
        return repository.findByPolicyIdAndIsDeletedFalse(policyId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PolicyAudit> findByRequestId(UUID requestId) {
        return repository.findByRequestIdAndIsDeletedFalse(requestId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PolicyAudit> findByUserId(String userId) {
        return repository.findByUserIdAndIsDeletedFalse(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PolicyAudit> findByDateRange(OffsetDateTime start, OffsetDateTime end) {
        return repository.findByTimestampBetweenAndIsDeletedFalse(start, end).stream().map(this::toDomain).toList();
    }

    @Override
    public List<PolicyAudit> findByDecision(PolicyDecision decision) {
        return repository.findByDecisionAndIsDeletedFalse(decision.name()).stream().map(this::toDomain).toList();
    }

    private PolicyAudit toDomain(PolicyAuditEntity e) {
        return new PolicyAudit(
            e.getId(), e.getPolicyId(), e.getRequestId(), e.getAction(),
            PolicyDecision.valueOf(e.getDecision()), new ArrayList<>(),
            new HashMap<>(), e.getUserId(), e.getProcessingTimeMs(),
            e.getSuccess(), e.getTimestamp(), e.getCreatedAt()
        );
    }
}
