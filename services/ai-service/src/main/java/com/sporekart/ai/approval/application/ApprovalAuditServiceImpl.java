package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalAuditService;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalAuditEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalAuditRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalAuditServiceImpl implements ApprovalAuditService {

    private final ApprovalAuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ApprovalAudit recordAudit(ApprovalAudit audit) {
        var entity = new ApprovalAuditEntity();
        entity.setId(audit.id() != null ? audit.id() : UUID.randomUUID());
        entity.setRequestId(audit.requestId());
        entity.setReviewerId(audit.reviewerId());
        entity.setAction(audit.action());
        entity.setDecision(audit.decision() != null ? audit.decision().name() : null);
        entity.setStatus(audit.status() != null ? audit.status().name() : null);
        entity.setDetails(toJson(audit.details()));
        entity.setUserId(audit.userId());
        entity.setProcessingTimeMs(audit.processingTimeMs());
        entity.setSuccess(audit.success());
        entity.setTimestamp(audit.timestamp() != null ? audit.timestamp() : OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = auditRepository.save(entity);
        log.info("Audit recorded for request: {}", audit.requestId());
        return toDomain(saved);
    }

    @Override
    public List<ApprovalAudit> findByRequestId(UUID requestId) {
        return auditRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalAudit> findByReviewerId(UUID reviewerId) {
        return auditRepository.findByReviewerIdAndIsDeletedFalse(reviewerId).stream()
            .map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalAudit> findByDateRange(OffsetDateTime start, OffsetDateTime end) {
        return auditRepository.findByTimestampBetweenAndIsDeletedFalse(start, end).stream()
            .map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalAudit> findByDecision(ApprovalDecision decision) {
        return auditRepository.findByDecisionAndIsDeletedFalse(decision.name()).stream()
            .map(this::toDomain).toList();
    }

    private ApprovalAudit toDomain(ApprovalAuditEntity entity) {
        return new ApprovalAudit(
            entity.getId(), entity.getRequestId(), entity.getReviewerId(),
            entity.getAction(),
            entity.getDecision() != null ? ApprovalDecision.valueOf(entity.getDecision()) : null,
            entity.getStatus() != null ? ApprovalStatus.valueOf(entity.getStatus()) : null,
            fromJson(entity.getDetails(), new TypeReference<Map<String, Object>>() {}),
            entity.getUserId(),
            entity.getProcessingTimeMs() != null ? entity.getProcessingTimeMs() : 0L,
            entity.getSuccess() != null && entity.getSuccess(),
            entity.getTimestamp(), entity.getCreatedAt()
        );
    }

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    private <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
