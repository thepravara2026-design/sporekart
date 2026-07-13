package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.*;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalRequestEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalEngineImplTest {

    @Mock private ApprovalRequestRepository requestRepository;
    @Mock private ApprovalAssignmentService assignmentService;
    @Mock private ApprovalDecisionService decisionService;
    @Mock private ApprovalHistoryService historyService;
    @Mock private ApprovalEscalationService escalationService;
    @Mock private ApprovalDelegationService delegationService;
    @Mock private ApprovalAuditService auditService;
    @Mock private ApprovalMetricsService metricsService;
    @Mock private ApprovalNotificationService notificationService;

    private ObjectMapper objectMapper;
    private ApprovalEngineImpl engine;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        engine = new ApprovalEngineImpl(requestRepository, assignmentService, decisionService,
            historyService, escalationService, delegationService, auditService,
            metricsService, notificationService, objectMapper);
    }

    private ApprovalRequest createTestRequest() {
        return new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null,
            ApprovalStatus.PENDING, OffsetDateTime.now());
    }

    @Test
    void shouldSubmitRequest() {
        var request = createTestRequest();
        var entity = new ApprovalRequestEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule(request.module());
        entity.setAction(request.action());
        entity.setUserId(request.userId());
        entity.setReason(request.reason());
        entity.setUrgency(request.urgency());
        entity.setStatus(ApprovalStatus.PENDING.name());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(requestRepository.save(any())).thenReturn(entity);
        var assignment = new ApprovalAssignment(UUID.randomUUID(), entity.getId(), null,
            "reviewer1", AssignmentStrategy.ROLE_BASED, 1, ApprovalStatus.ASSIGNED,
            OffsetDateTime.now(), null, null);
        when(assignmentService.assign(any())).thenReturn(assignment);

        var result = engine.submit(request);

        assertNotNull(result);
        assertEquals(entity.getModule(), result.module());
        verify(assignmentService).assign(any());
        verify(historyService).recordHistory(any());
        verify(notificationService).notifyAssigned(any());
        verify(metricsService).recordSubmission();
    }

    @Test
    void shouldApproveRequest() {
        var requestId = UUID.randomUUID();
        var reviewerId = "reviewer1";
        var comment = "Looks good";

        var approved = new ApprovalRequest(requestId, "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null,
            ApprovalStatus.APPROVED, OffsetDateTime.now());

        when(decisionService.approve(requestId, reviewerId, comment)).thenReturn(approved);

        var result = engine.approve(requestId, reviewerId, comment);

        assertEquals(ApprovalStatus.APPROVED, result.status());
        verify(decisionService).approve(requestId, reviewerId, comment);
        verify(historyService).recordHistory(any());
        verify(notificationService).notifyApproved(any());
        verify(metricsService).recordApproval(0);
    }

    @Test
    void shouldRejectRequest() {
        var requestId = UUID.randomUUID();
        var reviewerId = "reviewer1";
        var comment = "Not good";

        var rejected = new ApprovalRequest(requestId, "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null,
            ApprovalStatus.REJECTED, OffsetDateTime.now());

        when(decisionService.reject(requestId, reviewerId, comment)).thenReturn(rejected);

        var result = engine.reject(requestId, reviewerId, comment);

        assertEquals(ApprovalStatus.REJECTED, result.status());
        verify(decisionService).reject(requestId, reviewerId, comment);
        verify(historyService).recordHistory(any());
        verify(notificationService).notifyRejected(any());
        verify(metricsService).recordRejection(0);
    }

    @Test
    void shouldGetStatus() {
        var requestId = UUID.randomUUID();
        var entity = new ApprovalRequestEntity();
        entity.setId(requestId);
        entity.setModule("content");
        entity.setAction("publish");
        entity.setUserId("user1");
        entity.setStatus(ApprovalStatus.PENDING.name());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(requestRepository.findByIdAndIsDeletedFalse(requestId)).thenReturn(Optional.of(entity));

        var result = engine.getStatus(requestId);

        assertNotNull(result);
        assertEquals(requestId, result.id());
    }

    @Test
    void shouldGetPendingApprovals() {
        String userId = "reviewer1";
        var requestId = UUID.randomUUID();
        var assignment = new ApprovalAssignment(UUID.randomUUID(), requestId, UUID.randomUUID(),
            userId, AssignmentStrategy.ROLE_BASED, 1, ApprovalStatus.ASSIGNED,
            OffsetDateTime.now(), null, null);

        when(assignmentService.getPendingAssignments(userId)).thenReturn(List.of(assignment));

        var entity = new ApprovalRequestEntity();
        entity.setId(requestId);
        entity.setModule("content");
        entity.setAction("publish");
        entity.setUserId("user1");
        entity.setStatus(ApprovalStatus.PENDING.name());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(requestRepository.findByIdAndIsDeletedFalse(requestId)).thenReturn(Optional.of(entity));

        var results = engine.getPendingApprovals(userId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void shouldGetHistory() {
        var requestId = UUID.randomUUID();
        var history = new ApprovalHistory(UUID.randomUUID(), requestId, UUID.randomUUID(),
            ApprovalDecision.APPROVE, "approved", Map.of(), OffsetDateTime.now());

        when(historyService.getHistory(requestId)).thenReturn(List.of(history));

        var results = engine.getHistory(requestId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }
}
