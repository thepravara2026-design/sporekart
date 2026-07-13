package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.decision.domain.*;
import com.sporekart.ai.decision.infrastructure.persistence.DecisionAuditEntity;
import com.sporekart.ai.decision.infrastructure.persistence.DecisionAuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DecisionAuditServiceImplTest {

    @Mock private DecisionAuditRepository repository;

    private DecisionAuditServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DecisionAuditServiceImpl(repository);
    }

    @Test
    void recordAuditCallsSave() {
        UUID id = UUID.randomUUID();
        DecisionAudit audit = new DecisionAudit(id, UUID.randomUUID(), UUID.randomUUID(),
            DecisionAction.ALLOW, DecisionStatus.ALLOWED, DecisionConfidence.HIGH,
            List.of(), Map.of("k", "v"), "user1", 100L, true, OffsetDateTime.now(), OffsetDateTime.now());

        DecisionAudit result = service.recordAudit(audit);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(repository).save(any(DecisionAuditEntity.class));
    }

    @Test
    void findByRequestIdDelegatesToRepository() {
        UUID requestId = UUID.randomUUID();
        when(repository.findByRequestIdAndIsDeletedFalse(requestId)).thenReturn(List.of());

        List<DecisionAudit> results = service.findByRequestId(requestId);

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(repository).findByRequestIdAndIsDeletedFalse(requestId);
    }
}
