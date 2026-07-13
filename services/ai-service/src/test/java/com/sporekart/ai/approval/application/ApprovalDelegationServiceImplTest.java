package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalDelegationEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalDelegationRepository;
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
class ApprovalDelegationServiceImplTest {

    @Mock
    private ApprovalDelegationRepository delegationRepository;

    private ApprovalDelegationServiceImpl delegationService;

    @BeforeEach
    void setUp() {
        delegationService = new ApprovalDelegationServiceImpl(delegationRepository);
    }

    @Test
    void shouldDelegate() {
        var requestId = UUID.randomUUID();
        var fromId = UUID.randomUUID();
        var toId = UUID.randomUUID();

        when(delegationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = delegationService.delegate(requestId, fromId, toId, "Out of office");

        assertNotNull(result);
        assertEquals(requestId, result.requestId());
        assertEquals(fromId, result.fromReviewerId());
        assertEquals(toId, result.toReviewerId());
        assertTrue(result.isActive());
        verify(delegationRepository).save(any());
    }

    @Test
    void shouldRevokeDelegation() {
        var delegationId = UUID.randomUUID();
        var entity = new ApprovalDelegationEntity();
        entity.setId(delegationId);
        entity.setRequestId(UUID.randomUUID());
        entity.setFromReviewerId(UUID.randomUUID());
        entity.setToReviewerId(UUID.randomUUID());
        entity.setIsActive(true);
        entity.setDelegatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(delegationRepository.findById(delegationId)).thenReturn(Optional.of(entity));
        when(delegationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = delegationService.revokeDelegation(delegationId);

        assertFalse(result.isActive());
    }

    @Test
    void shouldGetActiveDelegations() {
        var entity = new ApprovalDelegationEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(UUID.randomUUID());
        entity.setFromReviewerId(UUID.randomUUID());
        entity.setToReviewerId(UUID.randomUUID());
        entity.setIsActive(true);
        entity.setDelegatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(delegationRepository.findByIsActiveTrue()).thenReturn(List.of(entity));

        var results = delegationService.getActiveDelegations("user1");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }
}
