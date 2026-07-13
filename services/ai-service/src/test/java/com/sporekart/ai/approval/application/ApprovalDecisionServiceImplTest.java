package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalHistoryEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalHistoryRepository;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalRequestEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalDecisionServiceImplTest {

    @Mock
    private ApprovalRequestRepository requestRepository;

    @Mock
    private ApprovalHistoryRepository historyRepository;

    private ObjectMapper objectMapper;
    private ApprovalDecisionServiceImpl decisionService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        decisionService = new ApprovalDecisionServiceImpl(requestRepository, historyRepository, objectMapper);
    }

    private ApprovalRequestEntity createRequestEntity(UUID id, String status) {
        var entity = new ApprovalRequestEntity();
        entity.setId(id);
        entity.setModule("content");
        entity.setAction("publish");
        entity.setUserId("user1");
        entity.setStatus(status);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        return entity;
    }

    @Test
    void shouldApproveRequest() {
        var requestId = UUID.randomUUID();
        var entity = createRequestEntity(requestId, ApprovalStatus.PENDING.name());

        when(requestRepository.findByIdAndIsDeletedFalse(requestId)).thenReturn(Optional.of(entity));
        when(requestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = decisionService.approve(requestId, "reviewer1", "approved");

        assertEquals(ApprovalStatus.APPROVED, result.status());
        assertEquals("APPROVED", entity.getStatus());
        verify(requestRepository).save(entity);
        verify(historyRepository).save(any());
    }

    @Test
    void shouldRejectRequest() {
        var requestId = UUID.randomUUID();
        var entity = createRequestEntity(requestId, ApprovalStatus.PENDING.name());

        when(requestRepository.findByIdAndIsDeletedFalse(requestId)).thenReturn(Optional.of(entity));
        when(requestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = decisionService.reject(requestId, "reviewer1", "rejected");

        assertEquals(ApprovalStatus.REJECTED, result.status());
        assertEquals("REJECTED", entity.getStatus());
    }

    @Test
    void shouldRequestChanges() {
        var requestId = UUID.randomUUID();
        var entity = createRequestEntity(requestId, ApprovalStatus.PENDING.name());

        when(requestRepository.findByIdAndIsDeletedFalse(requestId)).thenReturn(Optional.of(entity));
        when(requestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = decisionService.requestChanges(requestId, "reviewer1", "needs changes");

        assertEquals(ApprovalStatus.CHANGES_REQUESTED, result.status());
        assertEquals("CHANGES_REQUESTED", entity.getStatus());
    }

    @Test
    void isApprovedShouldReturnTrueWhenApproved() {
        var requestId = UUID.randomUUID();
        var entity = createRequestEntity(requestId, ApprovalStatus.APPROVED.name());

        when(requestRepository.findByIdAndIsDeletedFalse(requestId)).thenReturn(Optional.of(entity));

        assertTrue(decisionService.isApproved(requestId));
    }

    @Test
    void isApprovedShouldReturnFalseWhenNotApproved() {
        var requestId = UUID.randomUUID();
        var entity = createRequestEntity(requestId, ApprovalStatus.PENDING.name());

        when(requestRepository.findByIdAndIsDeletedFalse(requestId)).thenReturn(Optional.of(entity));

        assertFalse(decisionService.isApproved(requestId));
    }
}
