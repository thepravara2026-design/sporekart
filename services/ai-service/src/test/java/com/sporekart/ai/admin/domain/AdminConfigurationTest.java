package com.sporekart.ai.admin.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AdminConfigurationTest {

    @Test
    void testRecordConstruction() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var updatedBy = UUID.randomUUID();
        var config = new AdminConfiguration(id, "test.key", "test-value", "governance",
                "production", "Test configuration", ConfigurationStatus.ACTIVE, 2, updatedBy, now, now);

        assertEquals(id, config.id());
        assertEquals("test.key", config.key());
        assertEquals("test-value", config.value());
        assertEquals("governance", config.module());
        assertEquals("production", config.environment());
        assertEquals("Test configuration", config.description());
        assertEquals(ConfigurationStatus.ACTIVE, config.status());
        assertEquals(2, config.version());
        assertEquals(updatedBy, config.updatedBy());
        assertEquals(now, config.createdAt());
        assertEquals(now, config.updatedAt());
    }

    @Test
    void testEquality() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var config1 = new AdminConfiguration(id, "key", "val", "mod", "env",
                "desc", ConfigurationStatus.DRAFT, 1, null, now, now);
        var config2 = new AdminConfiguration(id, "key", "val", "mod", "env",
                "desc", ConfigurationStatus.DRAFT, 1, null, now, now);
        assertEquals(config1, config2);
        assertEquals(config1.hashCode(), config2.hashCode());
    }
}
