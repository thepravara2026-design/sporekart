package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingJobEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SemanticEmbeddingBatchService {

    private static final Logger log = LoggerFactory.getLogger(SemanticEmbeddingBatchService.class);

    private final SemanticEmbeddingJobRepository jobRepository;
    private final SemanticKafkaEventPublisher kafkaPublisher;

    public SemanticEmbeddingBatchService(SemanticEmbeddingJobRepository jobRepository,
                                          SemanticKafkaEventPublisher kafkaPublisher) {
        this.jobRepository = jobRepository;
        this.kafkaPublisher = kafkaPublisher;
    }

    @Transactional
    public SemanticEmbeddingJobEntity scheduleEmbeddingJob(String type, int totalItems, String config,
                                                            UUID createdBy) {
        SemanticEmbeddingJobEntity job = new SemanticEmbeddingJobEntity();
        job.setType(type);
        job.setStatus("PENDING");
        job.setTotalItems(totalItems);
        job.setProcessedItems(0);
        job.setFailedItems(0);
        job.setConfig(config);
        job.setCreatedBy(createdBy);
        job.setCreatedAt(OffsetDateTime.now());
        job = jobRepository.save(job);
        log.info("Scheduled embedding job: {} (type={}, items={})", job.getId(), type, totalItems);
        return job;
    }

    @Transactional
    public void processJob(UUID jobId) {
        SemanticEmbeddingJobEntity job = jobRepository.findByIdAndIsDeletedFalse(jobId)
                .orElseThrow(() -> new EmbeddingException("Job not found: " + jobId));
        job.setStatus("PROCESSING");
        job.setStartedAt(OffsetDateTime.now());
        jobRepository.save(job);
        log.info("Processing embedding job: {}", jobId);
    }

    @Transactional
    public void completeJob(UUID jobId) {
        SemanticEmbeddingJobEntity job = jobRepository.findByIdAndIsDeletedFalse(jobId)
                .orElseThrow(() -> new EmbeddingException("Job not found: " + jobId));
        job.setStatus("COMPLETED");
        job.setCompletedAt(OffsetDateTime.now());
        jobRepository.save(job);
        log.info("Completed embedding job: {}", jobId);
    }

    @Transactional
    public void failJob(UUID jobId, String errorMessage) {
        SemanticEmbeddingJobEntity job = jobRepository.findByIdAndIsDeletedFalse(jobId)
                .orElseThrow(() -> new EmbeddingException("Job not found: " + jobId));
        job.setStatus("FAILED");
        job.setErrorMessage(errorMessage);
        job.setCompletedAt(OffsetDateTime.now());
        jobRepository.save(job);
        log.error("Failed embedding job: {} - {}", jobId, errorMessage);
    }

    @Transactional
    public void incrementProcessed(UUID jobId) {
        SemanticEmbeddingJobEntity job = jobRepository.findByIdAndIsDeletedFalse(jobId)
                .orElseThrow(() -> new EmbeddingException("Job not found: " + jobId));
        job.setProcessedItems(job.getProcessedItems() + 1);
        jobRepository.save(job);
    }

    @Transactional
    public void incrementFailed(UUID jobId) {
        SemanticEmbeddingJobEntity job = jobRepository.findByIdAndIsDeletedFalse(jobId)
                .orElseThrow(() -> new EmbeddingException("Job not found: " + jobId));
        job.setFailedItems(job.getFailedItems() + 1);
        jobRepository.save(job);
    }

    public SemanticEmbeddingJobEntity getJobStatus(UUID jobId) {
        return jobRepository.findByIdAndIsDeletedFalse(jobId)
                .orElseThrow(() -> new EmbeddingException("Job not found: " + jobId));
    }

    @Transactional
    public void cancelJob(UUID jobId) {
        SemanticEmbeddingJobEntity job = jobRepository.findByIdAndIsDeletedFalse(jobId)
                .orElseThrow(() -> new EmbeddingException("Job not found: " + jobId));
        job.setStatus("CANCELLED");
        job.setCompletedAt(OffsetDateTime.now());
        jobRepository.save(job);
        log.info("Cancelled embedding job: {}", jobId);
    }

    public List<SemanticEmbeddingJobEntity> listJobs() {
        return jobRepository.findByIsDeletedFalse();
    }

    public List<SemanticEmbeddingJobEntity> findJobsByStatus(String status) {
        return jobRepository.findByStatusAndIsDeletedFalse(status);
    }
}
