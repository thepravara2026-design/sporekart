package com.sporekart.ai.automation.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EscalationManagerImplTest {

    private EscalationManagerImpl escalationManager;

    @BeforeEach
    void setUp() {
        escalationManager = new EscalationManagerImpl();
    }

    @Test
    void triggerEscalationShouldCreateEscalation() {
        var entityId = UUID.randomUUID();
        escalationManager.triggerEscalation(entityId, "policy", "critical issue");

        var active = escalationManager.getActiveEscalations();
        assertEquals(1, active.size());
        assertEquals(entityId, active.getFirst().entityId());
        assertEquals("policy", active.getFirst().entityType());
        assertEquals("critical issue", active.getFirst().reason());
        assertEquals(1, active.getFirst().level());
        assertFalse(active.getFirst().resolved());
    }

    @Test
    void getActiveEscalationsShouldReturnOnlyUnresolved() {
        var entityId1 = UUID.randomUUID();
        var entityId2 = UUID.randomUUID();
        escalationManager.triggerEscalation(entityId1, "policy", "issue1");
        escalationManager.triggerEscalation(entityId2, "config", "issue2");

        var all = escalationManager.getActiveEscalations();
        assertEquals(2, all.size());

        var id1 = all.getFirst().id();
        escalationManager.resolveEscalation(id1);

        var remaining = escalationManager.getActiveEscalations();
        assertEquals(1, remaining.size());
        assertNotEquals(id1, remaining.getFirst().id());
    }

    @Test
    void resolveEscalationShouldMarkAsResolved() {
        escalationManager.triggerEscalation(UUID.randomUUID(), "policy", "test");
        var active = escalationManager.getActiveEscalations();
        var id = active.getFirst().id();

        escalationManager.resolveEscalation(id);

        var after = escalationManager.getActiveEscalations();
        assertTrue(after.isEmpty());
    }

    @Test
    void resolveEscalationShouldDoNothingForUnknownId() {
        escalationManager.resolveEscalation(UUID.randomUUID());
        assertTrue(escalationManager.getActiveEscalations().isEmpty());
    }

    @Test
    void getActiveEscalationsShouldReturnEmptyWhenNone() {
        assertTrue(escalationManager.getActiveEscalations().isEmpty());
    }
}
