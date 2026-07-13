package com.sporekart.ai.policy.application;
import com.sporekart.ai.policy.domain.*;
import com.sporekart.ai.policy.infrastructure.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyLifecycleManagerImplTest {
    @Mock private PolicyRepository policyRepository;
    @Mock private PolicyVersionRepository versionRepository;
    private PolicyLifecycleManagerImpl manager;

    @BeforeEach void setUp() { manager = new PolicyLifecycleManagerImpl(policyRepository, versionRepository); }

    @Test void testCanTransition() {
        assertTrue(manager.canTransition(PolicyStatus.DRAFT, PolicyStatus.ACTIVE));
        assertTrue(manager.canTransition(PolicyStatus.ACTIVE, PolicyStatus.INACTIVE));
        assertTrue(manager.canTransition(PolicyStatus.INACTIVE, PolicyStatus.ACTIVE));
        assertTrue(manager.canTransition(PolicyStatus.ACTIVE, PolicyStatus.ARCHIVED));
        assertTrue(manager.canTransition(PolicyStatus.ARCHIVED, PolicyStatus.DEPRECATED));
        assertFalse(manager.canTransition(PolicyStatus.DRAFT, PolicyStatus.DEPRECATED));
    }

    @Test void testActivatePolicy() {
        UUID id = UUID.randomUUID();
        PolicyEntity entity = new PolicyEntity();
        entity.setId(id);
        entity.setStatus("DRAFT");
        when(policyRepository.findById(id)).thenReturn(Optional.of(entity));
        manager.activatePolicy(id, "admin");
        verify(policyRepository).save(entity);
        assertEquals("ACTIVE", entity.getStatus());
    }
}
