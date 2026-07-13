package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalEscalationService;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalEscalationEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalEscalationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Comparator;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalEscalationServiceImpl implements ApprovalEscalationService {

    private final ApprovalEscalationRepository escalationRepository;

    @Override
    @Transactional
    public ApprovalEscalation escalate(UUID requestId, UUID fromReviewerId, EscalationReason reason, String details) {
        var entity = new ApprovalEscalationEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(requestId);
        entity.setFromReviewerId(fromReviewerId);
        entity.setReason(reason.name());
        entity.setDetails(details);
        entity.setLevel(1);
        entity.setEscalatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = escalationRepository.save(entity);
        log.info("Escalation created for request {} by reviewer {}", requestId, fromReviewerId);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public ApprovalEscalation resolveEscalation(UUID escalationId, UUID newReviewerId) {
        var entity = escalationRepository.findById(escalationId)
            .orElseThrow(() -> new RuntimeException("Escalation not found: " + escalationId));
        entity.setToReviewerId(newReviewerId);
        entity.setResolvedAt(OffsetDateTime.now());
        var saved = escalationRepository.save(entity);
        log.info("Escalation {} resolved with new reviewer {}", escalationId, newReviewerId);
        return toDomain(saved);
    }

    @Override
    public List<ApprovalEscalation> getEscalations(UUID requestId) {
        return escalationRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalEscalation> getActiveEscalations() {
        return escalationRepository.findAll().stream()
            .filter(e -> e.getIsDeleted() == null || !e.getIsDeleted())
            .filter(e -> e.getResolvedAt() == null)
            .map(this::toDomain).toList();
    }

    @Override
    public Optional<ApprovalEscalation> getCurrentEscalation(UUID requestId) {
        return escalationRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .filter(e -> e.getResolvedAt() == null)
            .max(Comparator.comparing(ApprovalEscalationEntity::getEscalatedAt))
            .map(this::toDomain);
    }

    private ApprovalEscalation toDomain(ApprovalEscalationEntity entity) {
        return new ApprovalEscalation(
            entity.getId(), entity.getRequestId(), entity.getFromReviewerId(),
            entity.getToReviewerId(),
            entity.getReason() != null ? EscalationReason.valueOf(entity.getReason()) : null,
            entity.getDetails(),
            entity.getLevel() != null ? entity.getLevel() : 0,
            entity.getEscalatedAt(), entity.getResolvedAt()
        );
    }
}
