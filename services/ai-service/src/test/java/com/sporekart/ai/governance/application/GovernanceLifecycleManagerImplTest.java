package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.domain.*;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GovernanceLifecycleManagerImplTest {

    private final GovernanceLifecycleManagerImpl lifecycleManager = new GovernanceLifecycleManagerImpl();

    @Test
    void testTransition() {
        GovernancePolicy policy = new GovernancePolicy(UUID.randomUUID(), "test", "desc",
            GovernanceScope.ALL, GovernanceStatus.DRAFT, 100, Map.of(), Map.of(),
            true, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());

        GovernanceLifecycle event = lifecycleManager.transition(policy, "ACTIVE", "admin");
        assertNotNull(event);
        assertEquals("DRAFT", event.fromStatus());
        assertEquals("ACTIVE", event.toStatus());
    }

    @Test
    void testGetHistory() {
        assertTrue(lifecycleManager.getHistory(UUID.randomUUID()).isEmpty());
    }

    @Test
    void testCanTransition_Valid() {
        assertTrue(lifecycleManager.canTransition("DRAFT", "ACTIVE"));
        assertTrue(lifecycleManager.canTransition("ACTIVE", "INACTIVE"));
        assertTrue(lifecycleManager.canTransition("ACTIVE", "ARCHIVED"));
    }

    @Test
    void testCanTransition_Invalid() {
        assertFalse(lifecycleManager.canTransition("DRAFT", "ARCHIVED"));
        assertFalse(lifecycleManager.canTransition("DEPRECATED", "ACTIVE"));
    }
}
