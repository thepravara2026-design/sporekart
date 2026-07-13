package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalAuditEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalAuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalAuditServiceImplTest {

    @Mock
    private ApprovalAuditRepository auditRepository;

    private ObjectMapper objectMapper;
    private ApprovalAuditServiceImpl auditService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        auditService = new ApprovalAuditServiceImpl(auditRepository, objectMapper);
    }

    @Test
    void shouldRecordAudit() {
        var audit = new ApprovalAudit(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            "APPROVE", ApprovalDecision.APPROVE, ApprovalStatus.APPROVED,
            Map.of("key", "value"), "user1", 100L, true,
            OffsetDateTime.now(), OffsetDateTime.now());

        when(auditRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = auditService.recordAudit(audit);

        assertNotNull(result);
        assertEquals(audit.action(), result.action());
        assertEquals(audit.decision(), result.decision());
        verify(auditRepository).save(any());
    }

    @Test
    void shouldFindByRequestId() {
        var requestId = UUID.randomUUID();
        var entity = new ApprovalAuditEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(requestId);
        entity.setAction("APPROVE");
        entity.setDecision(ApprovalDecision.APPROVE.name());
        entity.setStatus(ApprovalStatus.APPROVED.name());
        entity.setUserId("user1");
        entity.setProcessingTimeMs(100L);
        entity.setSuccess(true);
        entity.setTimestamp(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(auditRepository.findByRequestIdAndIsDeletedFalse(requestId)).thenReturn(List.of(entity));

        var results = auditService.findByRequestId(requestId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void shouldFindByDateRange() {
        var start = OffsetDateTime.now().minusDays(1);
        var end = OffsetDateTime.now().plusDays(1);
        var entity = new ApprovalAuditEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(UUID.randomUUID());
        entity.setAction("APPROVE");
        entity.setUserId("user1");
        entity.setProcessingTimeMs(100L);
        entity.setSuccess(true);
        entity.setTimestamp(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(auditRepository.findByTimestampBetweenAndIsDeletedFalse(start, end)).thenReturn(List.of(entity));

        var results = auditService.findByDateRange(start, end);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }
}
