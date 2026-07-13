package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalAssignmentService;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalAssignmentEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalAssignmentRepository;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ApprovalAssignmentServiceImpl implements ApprovalAssignmentService {

    private final ApprovalAssignmentRepository assignmentRepository;
    private final ApprovalRequestRepository requestRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ApprovalAssignment assign(ApprovalRequest request) {
        var entity = new ApprovalAssignmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(request.id());
        entity.setReviewerUserId(request.userId());
        entity.setStrategy(AssignmentStrategy.ROLE_BASED.name());
        entity.setLevel(1);
        entity.setStatus(ApprovalStatus.ASSIGNED.name());
        entity.setAssignedAt(OffsetDateTime.now());
        entity.setDeadline(request.deadline());
        entity.setIsDeleted(false);
        var saved = assignmentRepository.save(entity);
        log.info("Assignment created for request: {}", request.id());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public ApprovalAssignment assignToReviewer(UUID requestId, UUID reviewerId) {
        var request = requestRepository.findByIdAndIsDeletedFalse(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));
        var entity = new ApprovalAssignmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(requestId);
        entity.setReviewerId(reviewerId);
        entity.setStrategy(AssignmentStrategy.DIRECT.name());
        entity.setLevel(1);
        entity.setStatus(ApprovalStatus.ASSIGNED.name());
        entity.setAssignedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = assignmentRepository.save(entity);
        log.info("Assigned request {} to reviewer {}", requestId, reviewerId);
        return toDomain(saved);
    }

    @Override
    public List<ApprovalAssignment> getAssignments(UUID requestId) {
        return assignmentRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .map(this::toDomain).toList();
    }

    @Override
    public Optional<ApprovalAssignment> getActiveAssignment(UUID requestId) {
        return assignmentRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .filter(e -> !List.of(ApprovalStatus.APPROVED.name(), ApprovalStatus.REJECTED.name(),
                ApprovalStatus.CANCELLED.name()).contains(e.getStatus()))
            .max(Comparator.comparing(ApprovalAssignmentEntity::getAssignedAt))
            .map(this::toDomain);
    }

    @Override
    @Transactional
    public void completeAssignment(UUID assignmentId, ApprovalDecision decision) {
        var entity = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new RuntimeException("Assignment not found: " + assignmentId));
        entity.setStatus(switch (decision) {
            case APPROVE -> ApprovalStatus.APPROVED.name();
            case REJECT -> ApprovalStatus.REJECTED.name();
            case REQUEST_CHANGES -> ApprovalStatus.CHANGES_REQUESTED.name();
            default -> ApprovalStatus.COMPLETED.name();
        });
        entity.setRespondedAt(OffsetDateTime.now());
        assignmentRepository.save(entity);
        log.info("Assignment {} completed with decision: {}", assignmentId, decision);
    }

    @Override
    public List<ApprovalAssignment> getPendingAssignments(String reviewerUserId) {
        return assignmentRepository.findByReviewerUserIdAndIsDeletedFalse(reviewerUserId).stream()
            .filter(e -> ApprovalStatus.ASSIGNED.name().equals(e.getStatus()))
            .map(this::toDomain).toList();
    }

    private ApprovalAssignment toDomain(ApprovalAssignmentEntity entity) {
        return new ApprovalAssignment(
            entity.getId(), entity.getRequestId(), entity.getReviewerId(),
            entity.getReviewerUserId(),
            entity.getStrategy() != null ? AssignmentStrategy.valueOf(entity.getStrategy()) : null,
            entity.getLevel() != null ? entity.getLevel() : 0,
            entity.getStatus() != null ? ApprovalStatus.valueOf(entity.getStatus()) : null,
            entity.getAssignedAt(), entity.getRespondedAt(), entity.getDeadline()
        );
    }
}
