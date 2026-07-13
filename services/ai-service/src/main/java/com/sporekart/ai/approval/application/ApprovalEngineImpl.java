package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.*;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalRequestEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalRequestRepository;
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
public class ApprovalEngineImpl implements ApprovalEngine {

    private final ApprovalRequestRepository requestRepository;
    private final ApprovalAssignmentService assignmentService;
    private final ApprovalDecisionService decisionService;
    private final ApprovalHistoryService historyService;
    private final ApprovalEscalationService escalationService;
    private final ApprovalDelegationService delegationService;
    private final ApprovalAuditService auditService;
    private final ApprovalMetricsService metricsService;
    private final ApprovalNotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ApprovalRequest submit(ApprovalRequest request) {
        var entity = new ApprovalRequestEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule(request.module());
        entity.setAction(request.action());
        entity.setPayload(toJson(request.payload()));
        entity.setContext(toJson(request.context()));
        entity.setUserId(request.userId());
        entity.setRoles(toJson(request.roles()));
        entity.setReason(request.reason());
        entity.setUrgency(request.urgency());
        entity.setDecisionId(request.decisionId());
        entity.setMetadata(toJson(request.metadata()));
        entity.setDeadline(request.deadline());
        entity.setStatus(ApprovalStatus.PENDING.name());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = requestRepository.save(entity);
        var createdRequest = toDomain(saved);

        var assignment = assignmentService.assign(createdRequest);
        historyService.recordHistory(new ApprovalHistory(
            UUID.randomUUID(), createdRequest.id(), null, null,
            "Request submitted", null, OffsetDateTime.now()
        ));
        notificationService.notifyAssigned(assignment);
        metricsService.recordSubmission();
        log.info("Approval request submitted: {}", createdRequest.id());
        return createdRequest;
    }

    @Override
    @Transactional
    public ApprovalRequest approve(UUID requestId, String reviewerId, String comment) {
        var result = decisionService.approve(requestId, reviewerId, comment);
        historyService.recordHistory(new ApprovalHistory(
            UUID.randomUUID(), requestId, UUID.fromString(reviewerId), ApprovalDecision.APPROVE,
            comment, null, OffsetDateTime.now()
        ));
        notificationService.notifyApproved(result);
        metricsService.recordApproval(0);
        log.info("Approval request approved: {}", requestId);
        return result;
    }

    @Override
    @Transactional
    public ApprovalRequest reject(UUID requestId, String reviewerId, String comment) {
        var result = decisionService.reject(requestId, reviewerId, comment);
        historyService.recordHistory(new ApprovalHistory(
            UUID.randomUUID(), requestId, UUID.fromString(reviewerId), ApprovalDecision.REJECT,
            comment, null, OffsetDateTime.now()
        ));
        notificationService.notifyRejected(result);
        metricsService.recordRejection(0);
        log.info("Approval request rejected: {}", requestId);
        return result;
    }

    @Override
    @Transactional
    public ApprovalRequest delegate(UUID requestId, UUID fromReviewerId, UUID toReviewerId, String reason) {
        delegationService.delegate(requestId, fromReviewerId, toReviewerId, reason);
        historyService.recordHistory(new ApprovalHistory(
            UUID.randomUUID(), requestId, fromReviewerId, ApprovalDecision.DELEGATE,
            reason, null, OffsetDateTime.now()
        ));
        metricsService.recordDelegation();
        log.info("Approval request delegated: {} from {} to {}", requestId, fromReviewerId, toReviewerId);
        return findRequestOrThrow(requestId);
    }

    @Override
    @Transactional
    public ApprovalRequest escalate(UUID requestId, UUID reviewerId, EscalationReason reason, String details) {
        escalationService.escalate(requestId, reviewerId, reason, details);
        historyService.recordHistory(new ApprovalHistory(
            UUID.randomUUID(), requestId, reviewerId, ApprovalDecision.ESCALATE,
            details, null, OffsetDateTime.now()
        ));
        metricsService.recordEscalation();
        log.info("Approval request escalated: {}", requestId);
        return findRequestOrThrow(requestId);
    }

    @Override
    @Transactional
    public ApprovalRequest cancel(UUID requestId, String userId, String reason) {
        var entity = requestRepository.findByIdAndIsDeletedFalse(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));
        entity.setStatus(ApprovalStatus.CANCELLED.name());
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = requestRepository.save(entity);
        var request = toDomain(saved);
        historyService.recordHistory(new ApprovalHistory(
            UUID.randomUUID(), requestId, null, ApprovalDecision.CANCEL,
            reason, null, OffsetDateTime.now()
        ));
        log.info("Approval request cancelled: {}", requestId);
        return request;
    }

    @Override
    public ApprovalRequest getStatus(UUID requestId) {
        return findRequestOrThrow(requestId);
    }

    @Override
    public List<ApprovalRequest> getPendingApprovals(String userId) {
        return assignmentService.getPendingAssignments(userId).stream()
            .map(a -> findRequestOrThrow(a.requestId()))
            .toList();
    }

    @Override
    public List<ApprovalHistory> getHistory(UUID requestId) {
        return historyService.getHistory(requestId);
    }

    private ApprovalRequest findRequestOrThrow(UUID requestId) {
        return requestRepository.findByIdAndIsDeletedFalse(requestId)
            .map(this::toDomain)
            .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));
    }

    private ApprovalRequest toDomain(ApprovalRequestEntity entity) {
        return new ApprovalRequest(
            entity.getId(), entity.getModule(), entity.getAction(),
            fromJson(entity.getPayload(), new TypeReference<Map<String, Object>>() {}),
            fromJson(entity.getContext(), new TypeReference<Map<String, Object>>() {}),
            entity.getUserId(),
            fromJson(entity.getRoles(), new TypeReference<List<String>>() {}),
            entity.getReason(), entity.getUrgency(),
            entity.getDecisionId(),
            fromJson(entity.getMetadata(), new TypeReference<Map<String, Object>>() {}),
            entity.getDeadline(),
            ApprovalStatus.valueOf(entity.getStatus()),
            entity.getCreatedAt()
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
