package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingJobEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingJobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingBatchProcessorTest {

    @Mock private SemanticEmbeddingJobRepository jobRepository;

    private SemanticEmbeddingBatchService batchService;

    @BeforeEach
    void setUp() {
        batchService = new SemanticEmbeddingBatchService(jobRepository, null);
    }

    @Test
    void testScheduleEmbeddingJob() {
        when(jobRepository.save(any())).thenAnswer(invocation -> {
            SemanticEmbeddingJobEntity entity = invocation.getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        SemanticEmbeddingJobEntity job = batchService.scheduleEmbeddingJob("BATCH", 50, "{}", UUID.randomUUID());

        assertNotNull(job);
        assertEquals("BATCH", job.getType());
        assertEquals(50, job.getTotalItems());
        assertEquals("PENDING", job.getStatus());
    }

    @Test
    void testProcessJob() {
        UUID jobId = UUID.randomUUID();
        SemanticEmbeddingJobEntity job = new SemanticEmbeddingJobEntity();
        job.setId(jobId);
        job.setStatus("PENDING");

        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.of(job));
        when(jobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        batchService.processJob(jobId);

        assertEquals("PROCESSING", job.getStatus());
    }

    @Test
    void testCompleteJob() {
        UUID jobId = UUID.randomUUID();
        SemanticEmbeddingJobEntity job = new SemanticEmbeddingJobEntity();
        job.setId(jobId);
        job.setStatus("PROCESSING");

        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.of(job));
        when(jobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        batchService.completeJob(jobId);

        assertEquals("COMPLETED", job.getStatus());
    }

    @Test
    void testFailJob() {
        UUID jobId = UUID.randomUUID();
        SemanticEmbeddingJobEntity job = new SemanticEmbeddingJobEntity();
        job.setId(jobId);
        job.setStatus("PROCESSING");

        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.of(job));
        when(jobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        batchService.failJob(jobId, "error occurred");

        assertEquals("FAILED", job.getStatus());
        assertEquals("error occurred", job.getErrorMessage());
    }

    @Test
    void testIncrementProcessed() {
        UUID jobId = UUID.randomUUID();
        SemanticEmbeddingJobEntity job = new SemanticEmbeddingJobEntity();
        job.setId(jobId);
        job.setProcessedItems(5);

        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.of(job));
        when(jobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        batchService.incrementProcessed(jobId);

        assertEquals(6, job.getProcessedItems());
    }

    @Test
    void testListJobs() {
        when(jobRepository.findByIsDeletedFalse()).thenReturn(List.of(new SemanticEmbeddingJobEntity()));
        assertEquals(1, batchService.listJobs().size());
    }

    @Test
    void testFindJobsByStatus() {
        when(jobRepository.findByStatusAndIsDeletedFalse("PENDING"))
                .thenReturn(List.of(new SemanticEmbeddingJobEntity()));
        assertEquals(1, batchService.findJobsByStatus("PENDING").size());
    }

    @Test
    void testCancelJob() {
        UUID jobId = UUID.randomUUID();
        SemanticEmbeddingJobEntity job = new SemanticEmbeddingJobEntity();
        job.setId(jobId);
        job.setStatus("PENDING");

        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.of(job));
        when(jobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        batchService.cancelJob(jobId);

        assertEquals("CANCELLED", job.getStatus());
    }
}
