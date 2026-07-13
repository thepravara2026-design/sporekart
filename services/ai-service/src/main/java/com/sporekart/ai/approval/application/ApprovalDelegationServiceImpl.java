package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalDelegationService;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalDelegationEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalDelegationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalDelegationServiceImpl implements ApprovalDelegationService {

    private final ApprovalDelegationRepository delegationRepository;

    @Override
    @Transactional
    public ApprovalDelegation delegate(UUID requestId, UUID fromReviewerId, UUID toReviewerId, String reason) {
        var entity = new ApprovalDelegationEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(requestId);
        entity.setFromReviewerId(fromReviewerId);
        entity.setToReviewerId(toReviewerId);
        entity.setReason(reason);
        entity.setIsActive(true);
        entity.setDelegatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = delegationRepository.save(entity);
        log.info("Delegation created for request {} from {} to {}", requestId, fromReviewerId, toReviewerId);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public ApprovalDelegation revokeDelegation(UUID delegationId) {
        var entity = delegationRepository.findById(delegationId)
            .orElseThrow(() -> new RuntimeException("Delegation not found: " + delegationId));
        entity.setIsActive(false);
        var saved = delegationRepository.save(entity);
        log.info("Delegation {} revoked", delegationId);
        return toDomain(saved);
    }

    @Override
    public List<ApprovalDelegation> getDelegations(UUID requestId) {
        return delegationRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalDelegation> getActiveDelegations(String userId) {
        return delegationRepository.findByIsActiveTrue().stream()
            .filter(e -> e.getIsDeleted() == null || !e.getIsDeleted())
            .map(this::toDomain).toList();
    }

    @Override
    public Optional<ApprovalDelegation> getActiveDelegation(UUID requestId) {
        return delegationRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .filter(e -> e.getIsActive() != null && e.getIsActive())
            .findFirst()
            .map(this::toDomain);
    }

    private ApprovalDelegation toDomain(ApprovalDelegationEntity entity) {
        return new ApprovalDelegation(
            entity.getId(), entity.getRequestId(), entity.getFromReviewerId(),
            entity.getToReviewerId(), entity.getReason(),
            entity.getIsActive() != null && entity.getIsActive(),
            entity.getDelegatedAt(), entity.getExpiresAt()
        );
    }
}
