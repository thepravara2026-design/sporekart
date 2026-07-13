package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.domain.*;
import com.sporekart.ai.governance.infrastructure.persistence.*;
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
class GovernanceManagerImplTest {

    @Mock private GovernanceRepository policyRepository;
    @Mock private GovernanceConfigurationRepository configRepository;
    private GovernanceManagerImpl manager;

    @BeforeEach
    void setUp() {
        manager = new GovernanceManagerImpl(policyRepository, configRepository);
    }

    @Test
    void testCreatePolicy() {
        GovernancePolicy policy = new GovernancePolicy(UUID.randomUUID(), "test", "desc",
            GovernanceScope.ALL, GovernanceStatus.ACTIVE, 100, Map.of(), Map.of(),
            true, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(policyRepository.save(any())).thenReturn(new GovernanceEntity());
        GovernancePolicy result = manager.createPolicy(policy);
        assertEquals("test", result.name());
    }

    @Test
    void testGetPolicy_NotFound() {
        when(policyRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Optional.empty());
        assertTrue(manager.getPolicy(UUID.randomUUID()).isEmpty());
    }

    @Test
    void testListPolicies() {
        when(policyRepository.findByIsDeletedFalse()).thenReturn(List.of());
        assertTrue(manager.listPolicies().isEmpty());
    }

    @Test
    void testReloadConfiguration() {
        assertDoesNotThrow(() -> manager.reloadConfiguration());
    }
}
