package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalDecisionService;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalHistoryEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalHistoryRepository;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalRequestEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalDecisionServiceImpl implements ApprovalDecisionService {

    private final ApprovalRequestRepository requestRepository;
    private final ApprovalHistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ApprovalRequest approve(UUID requestId, String reviewerId, String comment) {
        var entity = findRequest(requestId);
        entity.setStatus(ApprovalStatus.APPROVED.name());
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = requestRepository.save(entity);
        saveHistory(requestId, reviewerId, ApprovalDecision.APPROVE, comment);
        log.info("Request {} approved by {}", requestId, reviewerId);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public ApprovalRequest reject(UUID requestId, String reviewerId, String comment) {
        var entity = findRequest(requestId);
        entity.setStatus(ApprovalStatus.REJECTED.name());
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = requestRepository.save(entity);
        saveHistory(requestId, reviewerId, ApprovalDecision.REJECT, comment);
        log.info("Request {} rejected by {}", requestId, reviewerId);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public ApprovalRequest requestChanges(UUID requestId, String reviewerId, String comment) {
        var entity = findRequest(requestId);
        entity.setStatus(ApprovalStatus.CHANGES_REQUESTED.name());
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = requestRepository.save(entity);
        saveHistory(requestId, reviewerId, ApprovalDecision.REQUEST_CHANGES, comment);
        log.info("Changes requested for request {} by {}", requestId, reviewerId);
        return toDomain(saved);
    }

    @Override
    public boolean isApproved(UUID requestId) {
        return findRequest(requestId).getStatus().equals(ApprovalStatus.APPROVED.name());
    }

    @Override
    public boolean isRejected(UUID requestId) {
        return findRequest(requestId).getStatus().equals(ApprovalStatus.REJECTED.name());
    }

    @Override
    public ApprovalDecision getCurrentDecision(UUID requestId) {
        return historyRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .filter(e -> e.getDecision() != null)
            .max(Comparator.comparing(ApprovalHistoryEntity::getTimestamp))
            .map(e -> ApprovalDecision.valueOf(e.getDecision()))
            .orElse(null);
    }

    private ApprovalRequestEntity findRequest(UUID requestId) {
        return requestRepository.findByIdAndIsDeletedFalse(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));
    }

    private void saveHistory(UUID requestId, String reviewerId, ApprovalDecision decision, String comment) {
        var history = new ApprovalHistoryEntity();
        history.setId(UUID.randomUUID());
        history.setRequestId(requestId);
        history.setReviewerId(reviewerId != null ? UUID.fromString(reviewerId) : null);
        history.setDecision(decision.name());
        history.setComment(comment);
        history.setTimestamp(OffsetDateTime.now());
        history.setIsDeleted(false);
        historyRepository.save(history);
    }

    private ApprovalRequest toDomain(ApprovalRequestEntity entity) {
        return new ApprovalRequest(
            entity.getId(), entity.getModule(), entity.getAction(), null, null,
            entity.getUserId(), null, entity.getReason(), entity.getUrgency(),
            entity.getDecisionId(), null, entity.getDeadline(),
            ApprovalStatus.valueOf(entity.getStatus()), entity.getCreatedAt()
        );
    }
}
