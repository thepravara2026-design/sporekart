package com.sporekart.ai.admin.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class FeatureFlagTest {

    @Test
    void testRecordConstruction() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var metadata = Map.of("scope", "all");
        var flag = new FeatureFlag(id, "test-flag", "Test Flag", "A test flag",
                true, "production", "governance", metadata, now, now);

        assertEquals(id, flag.id());
        assertEquals("test-flag", flag.key());
        assertEquals("Test Flag", flag.name());
        assertEquals("A test flag", flag.description());
        assertTrue(flag.enabled());
        assertEquals("production", flag.environment());
        assertEquals("governance", flag.module());
        assertEquals(metadata, flag.metadata());
        assertEquals(now, flag.createdAt());
        assertEquals(now, flag.updatedAt());
    }

    @Test
    void testDisabledFlag() {
        var flag = new FeatureFlag(UUID.randomUUID(), "flag", "Flag", "",
                false, "dev", "core", Map.of(), Instant.now(), Instant.now());
        assertFalse(flag.enabled());
    }
}
