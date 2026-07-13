package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.GovernanceAuditService;
import com.sporekart.ai.governance.domain.*;
import com.sporekart.ai.governance.infrastructure.persistence.GovernanceAuditEntity;
import com.sporekart.ai.governance.infrastructure.persistence.GovernanceAuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernanceAuditServiceImplTest {

    @Mock private GovernanceAuditRepository repository;
    private GovernanceAuditServiceImpl auditService;

    @BeforeEach
    void setUp() {
        auditService = new GovernanceAuditServiceImpl(repository);
    }

    @Test
    void testRecordAudit() {
        GovernanceAudit audit = new GovernanceAudit(UUID.randomUUID(), UUID.randomUUID(),
            "validate", "content", GovernanceDecision.ALLOW, List.of(), Map.of(),
            "user1", 100L, true, OffsetDateTime.now(), OffsetDateTime.now());

        when(repository.save(any())).thenReturn(new GovernanceAuditEntity());
        GovernanceAudit result = auditService.recordAudit(audit);
        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void testFindByRequestId() {
        UUID requestId = UUID.randomUUID();
        when(repository.findByRequestIdAndIsDeletedFalse(requestId)).thenReturn(List.of());
        assertTrue(auditService.findByRequestId(requestId).isEmpty());
    }

    @Test
    void testFindByUserId() {
        when(repository.findByUserIdAndIsDeletedFalse("user1")).thenReturn(List.of());
        assertTrue(auditService.findByUserId("user1").isEmpty());
    }
}
