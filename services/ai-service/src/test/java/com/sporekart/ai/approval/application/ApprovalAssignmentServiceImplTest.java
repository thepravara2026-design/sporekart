package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalAssignmentEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalAssignmentRepository;
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
class ApprovalAssignmentServiceImplTest {

    @Mock
    private ApprovalAssignmentRepository assignmentRepository;

    @Mock
    private ApprovalRequestRepository requestRepository;

    private ObjectMapper objectMapper;
    private ApprovalAssignmentServiceImpl assignmentService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        assignmentService = new ApprovalAssignmentServiceImpl(assignmentRepository, requestRepository, objectMapper);
    }

    @Test
    void shouldAssign() {
        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.PENDING, OffsetDateTime.now());

        when(assignmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = assignmentService.assign(request);

        assertNotNull(result);
        assertEquals(request.id(), result.requestId());
        assertEquals("user1", result.reviewerUserId());
        verify(assignmentRepository).save(any());
    }

    @Test
    void shouldAssignToReviewer() {
        var requestId = UUID.randomUUID();
        var reviewerId = UUID.randomUUID();
        var requestEntity = new ApprovalRequestEntity();
        requestEntity.setId(requestId);
        requestEntity.setIsDeleted(false);

        when(requestRepository.findByIdAndIsDeletedFalse(requestId)).thenReturn(Optional.of(requestEntity));
        when(assignmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = assignmentService.assignToReviewer(requestId, reviewerId);

        assertNotNull(result);
        assertEquals(requestId, result.requestId());
        assertEquals(reviewerId, result.reviewerId());
    }

    @Test
    void shouldCompleteAssignment() {
        var assignmentId = UUID.randomUUID();
        var entity = new ApprovalAssignmentEntity();
        entity.setId(assignmentId);
        entity.setRequestId(UUID.randomUUID());
        entity.setStatus(ApprovalStatus.ASSIGNED.name());
        entity.setLevel(1);
        entity.setIsDeleted(false);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(entity));
        when(assignmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assignmentService.completeAssignment(assignmentId, ApprovalDecision.APPROVE);

        assertEquals(ApprovalStatus.APPROVED.name(), entity.getStatus());
        assertNotNull(entity.getRespondedAt());
    }

    @Test
    void shouldGetPendingAssignments() {
        var reviewerUserId = "reviewer1";
        var entity = new ApprovalAssignmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(UUID.randomUUID());
        entity.setReviewerUserId(reviewerUserId);
        entity.setStrategy(AssignmentStrategy.ROLE_BASED.name());
        entity.setLevel(1);
        entity.setStatus(ApprovalStatus.ASSIGNED.name());
        entity.setAssignedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(assignmentRepository.findByReviewerUserIdAndIsDeletedFalse(reviewerUserId)).thenReturn(List.of(entity));

        var results = assignmentService.getPendingAssignments(reviewerUserId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }
}
