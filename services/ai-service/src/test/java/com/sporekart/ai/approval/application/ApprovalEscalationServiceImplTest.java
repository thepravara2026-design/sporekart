package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalEscalationEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalEscalationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalEscalationServiceImplTest {

    @Mock
    private ApprovalEscalationRepository escalationRepository;

    private ApprovalEscalationServiceImpl escalationService;

    @BeforeEach
    void setUp() {
        escalationService = new ApprovalEscalationServiceImpl(escalationRepository);
    }

    @Test
    void shouldEscalate() {
        var requestId = UUID.randomUUID();
        var reviewerId = UUID.randomUUID();

        when(escalationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = escalationService.escalate(requestId, reviewerId, EscalationReason.TIMEOUT, "No response");

        assertNotNull(result);
        assertEquals(requestId, result.requestId());
        assertEquals(EscalationReason.TIMEOUT, result.reason());
        verify(escalationRepository).save(any());
    }

    @Test
    void shouldResolveEscalation() {
        var escalationId = UUID.randomUUID();
        var entity = new ApprovalEscalationEntity();
        entity.setId(escalationId);
        entity.setRequestId(UUID.randomUUID());
        entity.setFromReviewerId(UUID.randomUUID());
        entity.setReason(EscalationReason.TIMEOUT.name());
        entity.setLevel(1);
        entity.setEscalatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(escalationRepository.findById(escalationId)).thenReturn(Optional.of(entity));
        when(escalationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var newReviewerId = UUID.randomUUID();
        var result = escalationService.resolveEscalation(escalationId, newReviewerId);

        assertNotNull(result);
        assertEquals(newReviewerId, result.toReviewerId());
        assertNotNull(result.resolvedAt());
    }

    @Test
    void shouldGetActiveEscalations() {
        var entity = new ApprovalEscalationEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(UUID.randomUUID());
        entity.setFromReviewerId(UUID.randomUUID());
        entity.setReason(EscalationReason.TIMEOUT.name());
        entity.setLevel(1);
        entity.setEscalatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(escalationRepository.findAll()).thenReturn(List.of(entity));

        var results = escalationService.getActiveEscalations();

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }
}
