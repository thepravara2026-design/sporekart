package com.sporekart.ai.automation.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LifecycleDefinitionTest {

    @Test
    void shouldConstructRecord() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var transitions = Map.of(
            LifecycleStateType.CREATED, Map.of("approve", LifecycleStateType.ACTIVE)
        );
        var config = Map.<String, Object>of("key", "value");

        var def = new LifecycleDefinition(
            id, "test-lifecycle", "policy", LifecycleStateType.CREATED,
            transitions, config, now, now
        );

        assertEquals(id, def.id());
        assertEquals("test-lifecycle", def.name());
        assertEquals("policy", def.entityType());
        assertEquals(LifecycleStateType.CREATED, def.initialState());
        assertEquals(transitions, def.transitions());
        assertEquals(config, def.config());
        assertEquals(now, def.createdAt());
        assertEquals(now, def.updatedAt());
    }

    @Test
    void shouldSupportNullTransitions() {
        var def = new LifecycleDefinition(
            UUID.randomUUID(), "test", "policy", LifecycleStateType.ACTIVE,
            null, null, Instant.now(), Instant.now()
        );
        assertNull(def.transitions());
    }
}
