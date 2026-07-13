package com.sporekart.ai.policy.application;
import com.sporekart.ai.policy.domain.*;
import com.sporekart.ai.policy.infrastructure.persistence.PolicyAuditRepository;
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
class PolicyAuditServiceImplTest {
    @Mock private PolicyAuditRepository repository;
    private PolicyAuditServiceImpl auditService;

    @BeforeEach void setUp() { auditService = new PolicyAuditServiceImpl(repository); }

    @Test void testRecordAudit() {
        PolicyAudit a = new PolicyAudit(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "eval",
            PolicyDecision.ALLOW, List.of(), Map.of(), "user1", 100L, true, OffsetDateTime.now(), OffsetDateTime.now());
        when(repository.save(any())).thenReturn(new com.sporekart.ai.policy.infrastructure.persistence.PolicyAuditEntity());
        assertNotNull(auditService.recordAudit(a));
    }

    @Test void testFindByPolicyId() { when(repository.findByPolicyIdAndIsDeletedFalse(any())).thenReturn(List.of()); assertTrue(auditService.findByPolicyId(UUID.randomUUID()).isEmpty()); }
    @Test void testFindByUserId() { when(repository.findByUserIdAndIsDeletedFalse(any())).thenReturn(List.of()); assertTrue(auditService.findByUserId("u").isEmpty()); }
}
