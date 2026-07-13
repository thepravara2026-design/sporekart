package com.sporekart.ai.automation.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.automation.domain.AutomationJob;
import com.sporekart.ai.automation.domain.AutomationStatus;
import com.sporekart.ai.automation.domain.JobType;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AutomationJobRepositoryTest {

    @Autowired
    private AutomationJobRepository jobRepository;

    @Test
    void shouldSaveAndFindById() {
        var job = new AutomationJob(
            UUID.randomUUID(), JobType.HEALTH_CHECK, "test-job", Map.of("key", "value"),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null
        );

        var saved = jobRepository.save(job);

        assertNotNull(saved);
        assertEquals(job.id(), saved.id());
        assertEquals(JobType.HEALTH_CHECK, saved.type());
        assertEquals("test-job", saved.name());
        assertEquals(AutomationStatus.PENDING, saved.status());
    }

    @Test
    void shouldFindByStatus() {
        var pending = new AutomationJob(
            UUID.randomUUID(), JobType.CONFIG_SYNC, "pending-job", Map.of(),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null
        );
        var completed = new AutomationJob(
            UUID.randomUUID(), JobType.HEALTH_CHECK, "completed-job", Map.of(),
            AutomationStatus.COMPLETED, 0, 3, Instant.now(), Instant.now(), Instant.now(), null
        );

        jobRepository.save(pending);
        jobRepository.save(completed);

        var pendingJobs = jobRepository.findByStatus(AutomationStatus.PENDING);
        var completedJobs = jobRepository.findByStatus(AutomationStatus.COMPLETED);

        assertEquals(1, pendingJobs.size());
        assertEquals("pending-job", pendingJobs.getFirst().name());
        assertEquals(1, completedJobs.size());
        assertEquals("completed-job", completedJobs.getFirst().name());
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingStatus() {
        var jobs = jobRepository.findByStatus(AutomationStatus.FAILED);
        assertTrue(jobs.isEmpty());
    }

    @Test
    void shouldUpdateExistingJob() {
        var job = new AutomationJob(
            UUID.randomUUID(), JobType.HEALTH_CHECK, "original", Map.of(),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null
        );
        var saved = jobRepository.save(job);

        var updated = new AutomationJob(
            saved.id(), saved.type(), saved.name(), saved.params(),
            AutomationStatus.RUNNING, saved.retryCount(), saved.maxRetries(),
            saved.scheduledAt(), Instant.now(), null, null
        );
        jobRepository.save(updated);

        var found = jobRepository.findById(saved.id()).orElseThrow();
        assertEquals(AutomationStatus.RUNNING, found.status());
        assertNotNull(found.startedAt());
    }

    @Test
    void shouldDeleteJob() {
        var job = new AutomationJob(
            UUID.randomUUID(), JobType.HEALTH_CHECK, "to-delete", Map.of(),
            AutomationStatus.PENDING, 0, 3, Instant.now(), null, null, null
        );
        var saved = jobRepository.save(job);

        jobRepository.deleteById(saved.id());

        assertFalse(jobRepository.findById(saved.id()).isPresent());
    }
}
