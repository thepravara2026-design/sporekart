package com.sporekart.ai.automation.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AutomationJobTest {

    @Test
    void shouldConstructRecord() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var params = Map.<String, Object>of("target", "test");

        var job = new AutomationJob(
            id, JobType.HEALTH_CHECK, "health-check-job", params,
            AutomationStatus.PENDING, 0, 3, now, null, null, null
        );

        assertEquals(id, job.id());
        assertEquals(JobType.HEALTH_CHECK, job.type());
        assertEquals("health-check-job", job.name());
        assertEquals(params, job.params());
        assertEquals(AutomationStatus.PENDING, job.status());
        assertEquals(0, job.retryCount());
        assertEquals(3, job.maxRetries());
        assertEquals(now, job.scheduledAt());
        assertNull(job.startedAt());
        assertNull(job.completedAt());
        assertNull(job.errorMessage());
    }

    @Test
    void shouldSupportCompleteState() {
        var now = Instant.now();
        var job = new AutomationJob(
            UUID.randomUUID(), JobType.CONFIG_SYNC, "sync", Map.of(),
            AutomationStatus.COMPLETED, 1, 3, now, now, now, null
        );
        assertEquals(AutomationStatus.COMPLETED, job.status());
        assertEquals(1, job.retryCount());
        assertEquals(now, job.completedAt());
    }
}
