package com.sporekart.ai.content.application;

import com.sporekart.ai.content.api.ContentGenerationService;
import com.sporekart.ai.content.api.ContentPipelineExecutor;
import com.sporekart.ai.content.domain.*;
import com.sporekart.ai.content.infrastructure.kafka.ContentKafkaEventPublisher;
import com.sporekart.ai.content.infrastructure.monitoring.ContentMonitoringService;
import com.sporekart.ai.content.infrastructure.persistence.ContentHistoryEntity;
import com.sporekart.ai.content.infrastructure.persistence.ContentHistoryRepository;
import com.sporekart.ai.content.infrastructure.persistence.ContentResponseEntity;
import com.sporekart.ai.content.infrastructure.persistence.ContentResponseRepository;
import com.sporekart.ai.core.domain.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContentGenerationServiceImpl implements ContentGenerationService {

    private static final Logger log = LoggerFactory.getLogger(ContentGenerationServiceImpl.class);

    private final ContentPipelineExecutor pipelineExecutor;
    private final ContentResponseRepository responseRepository;
    private final ContentHistoryRepository historyRepository;
    private final ContentKafkaEventPublisher eventPublisher;
    private final ContentMonitoringService monitoringService;

    public ContentGenerationServiceImpl(ContentPipelineExecutor pipelineExecutor,
                                        ContentResponseRepository responseRepository,
                                        ContentHistoryRepository historyRepository,
                                        ContentKafkaEventPublisher eventPublisher,
                                        ContentMonitoringService monitoringService) {
        this.pipelineExecutor = pipelineExecutor;
        this.responseRepository = responseRepository;
        this.historyRepository = historyRepository;
        this.eventPublisher = eventPublisher;
        this.monitoringService = monitoringService;
    }

    @Override
    @Transactional
    public ContentGenerationResponse generate(ContentGenerationRequest request) {
        log.info("Generating content for request {}", request.id());
        var response = pipelineExecutor.executeGenerationPipeline(request);
        var entity = toEntity(request.id(), response);
        responseRepository.save(entity);
        eventPublisher.publishContentGenerated(request.id(), request.contentType().name());
        monitoringService.recordGenerationCompleted();
        log.info("Content generation completed for request {} with id {}", request.id(), response.id());
        return response;
    }

    @Override
    @Transactional
    public ContentGenerationResponse summarize(ContentSummaryRequest request) {
        var genRequest = new ContentGenerationRequest(
                request.id(),
                UUID.randomUUID().toString(),
                request.sourceText(),
                ContentType.TEXT,
                ContentCategory.GENERAL,
                ContentTone.NEUTRAL,
                request.language(),
                request.maxLength(),
                null,
                null,
                null
        );
        var result = pipelineExecutor.executeSummaryPipeline(request);
        var response = new ContentGenerationResponse(
                result.id(),
                request.id().toString(),
                result.summary(),
                ContentType.TEXT,
                ContentCategory.GENERAL,
                ContentTone.NEUTRAL,
                result.summaryLength(),
                0.95,
                false,
                ModerationStatus.APPROVED,
                result.generatedAt(),
                0,
                true,
                null
        );
        var entity = toEntity(request.id(), response);
        responseRepository.save(entity);
        eventPublisher.publishContentGenerated(request.id(), null);
        monitoringService.recordGenerationCompleted();
        return response;
    }

    @Override
    @Transactional
    public ContentGenerationResponse translate(ContentTranslationRequest request) {
        var result = pipelineExecutor.executeTranslationPipeline(request);
        var response = new ContentGenerationResponse(
                result.id(),
                request.id().toString(),
                result.translatedText(),
                ContentType.TEXT,
                ContentCategory.GENERAL,
                ContentTone.NEUTRAL,
                result.translatedText().length(),
                result.confidenceScore(),
                false,
                ModerationStatus.APPROVED,
                result.generatedAt(),
                0,
                true,
                null
        );
        var entity = toEntity(request.id(), response);
        responseRepository.save(entity);
        eventPublisher.publishContentGenerated(request.id(), null);
        monitoringService.recordGenerationCompleted();
        return response;
    }

    @Override
    @Transactional
    public ContentGenerationResponse classify(ContentClassificationRequest request) {
        var result = pipelineExecutor.executeClassificationPipeline(request);
        var response = new ContentGenerationResponse(
                result.id(),
                request.id().toString(),
                request.text(),
                ContentType.TEXT,
                ContentCategory.valueOf(result.primaryCategory().toUpperCase()),
                ContentTone.NEUTRAL,
                0,
                result.confidenceScore(),
                false,
                ModerationStatus.APPROVED,
                result.generatedAt(),
                0,
                true,
                null
        );
        var entity = toEntity(request.id(), response);
        responseRepository.save(entity);
        eventPublisher.publishContentGenerated(request.id(), null);
        monitoringService.recordGenerationCompleted();
        return response;
    }

    @Override
    @Transactional
    public ContentGenerationResponse moderate(ContentModerationRequest request) {
        var result = pipelineExecutor.executeModerationPipeline(request);
        var response = new ContentGenerationResponse(
                result.id(),
                request.id().toString(),
                request.content(),
                request.contentType(),
                ContentCategory.GENERAL,
                ContentTone.NEUTRAL,
                0,
                result.confidenceScore(),
                result.humanReviewRequired(),
                result.status(),
                result.reviewedAt() != null ? result.reviewedAt() : OffsetDateTime.now(),
                0,
                result.status() == ModerationStatus.APPROVED,
                null
        );
        var entity = toEntity(request.id(), response);
        responseRepository.save(entity);
        eventPublisher.publishContentGenerated(request.id(), null);
        monitoringService.recordGenerationCompleted();
        return response;
    }

    @Override
    @Transactional
    public ContentGenerationResponse generateSEO(ContentSEORequest request) {
        var result = pipelineExecutor.executeSEOPipeline(request);
        var response = new ContentGenerationResponse(
                result.id(),
                request.id().toString(),
                result.title() + "\n\n" + result.metaDescription(),
                request.contentType(),
                ContentCategory.SEO,
                ContentTone.PROFESSIONAL,
                0,
                result.seoScore() / 100.0,
                false,
                ModerationStatus.APPROVED,
                result.generatedAt(),
                0,
                true,
                null
        );
        var entity = toEntity(request.id(), response);
        responseRepository.save(entity);
        eventPublisher.publishContentGenerated(request.id(), null);
        monitoringService.recordGenerationCompleted();
        return response;
    }

    @Override
    public Optional<ContentGenerationResponse> getGeneration(UUID id) {
        return responseRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<ContentGenerationResponse> listHistory(UUID userId, int page, int size) {
        return historyRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .skip((long) page * size)
                .limit(size)
                .map(this::toDomainFromHistory)
                .toList();
    }

    private ContentResponseEntity toEntity(UUID requestId, ContentGenerationResponse response) {
        var entity = new ContentResponseEntity();
        entity.setId(response.id());
        entity.setRequestId(requestId);
        entity.setContent(response.content());
        entity.setContentType(response.contentType().name());
        entity.setCategory(response.category().name());
        entity.setTokenCount(response.tokenCount());
        entity.setConfidenceScore(response.confidenceScore());
        entity.setHumanReviewRequired(response.humanReviewRequired());
        entity.setModerationStatus(response.moderationStatus().name());
        entity.setGeneratedAt(response.generatedAt());
        entity.setLatencyMs(response.latencyMs());
        entity.setSuccess(response.success());
        entity.setErrorMessage(response.errorMessage());
        entity.setCreatedAt(OffsetDateTime.now());
        return entity;
    }

    private ContentGenerationResponse toDomain(ContentResponseEntity entity) {
        return new ContentGenerationResponse(
                entity.getId(),
                entity.getRequestId().toString(),
                entity.getContent(),
                ContentType.valueOf(entity.getContentType()),
                ContentCategory.valueOf(entity.getCategory()),
                ContentTone.NEUTRAL,
                entity.getTokenCount(),
                entity.getConfidenceScore(),
                entity.isHumanReviewRequired(),
                ModerationStatus.valueOf(entity.getModerationStatus()),
                entity.getGeneratedAt(),
                entity.getLatencyMs(),
                entity.isSuccess(),
                entity.getErrorMessage()
        );
    }

    private ContentGenerationResponse toDomainFromHistory(ContentHistoryEntity entity) {
        return new ContentGenerationResponse(
                entity.getId(),
                entity.getRequestId() != null ? entity.getRequestId().toString() : null,
                entity.getResultSummary(),
                ContentType.valueOf(entity.getContentType()),
                ContentCategory.GENERAL,
                ContentTone.NEUTRAL,
                entity.getTokenCount(),
                0.0,
                false,
                ModerationStatus.APPROVED,
                entity.getCreatedAt(),
                entity.getLatencyMs(),
                entity.isSuccess(),
                null
        );
    }
}
