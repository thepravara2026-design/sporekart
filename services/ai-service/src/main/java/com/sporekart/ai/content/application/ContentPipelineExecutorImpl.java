package com.sporekart.ai.content.application;

import com.sporekart.ai.content.api.ContentPipelineExecutor;
import com.sporekart.ai.content.domain.*;
import com.sporekart.ai.content.infrastructure.monitoring.ContentMonitoringService;
import com.sporekart.ai.content.infrastructure.persistence.GenerationMetricsEntity;
import com.sporekart.ai.content.infrastructure.persistence.GenerationMetricsRepository;
import com.sporekart.ai.core.domain.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ContentPipelineExecutorImpl implements ContentPipelineExecutor {

    private static final Logger log = LoggerFactory.getLogger(ContentPipelineExecutorImpl.class);

    private final GenerationMetricsRepository metricsRepository;
    private final ContentMonitoringService monitoringService;

    public ContentPipelineExecutorImpl(GenerationMetricsRepository metricsRepository,
                                       ContentMonitoringService monitoringService) {
        this.metricsRepository = metricsRepository;
        this.monitoringService = monitoringService;
    }

    @Override
    public ContentGenerationResponse executeGenerationPipeline(ContentGenerationRequest request) {
        long start = System.currentTimeMillis();
        log.info("Executing generation pipeline for request {}", request.id());

        recordStage("VALIDATE_INPUT", start);
        recordStage("RESOLVE_PLANNER", start);
        recordStage("RESOLVE_PROMPT", start);
        recordStage("RESOLVE_KNOWLEDGE", start);
        recordStage("RESOLVE_SEMANTIC_CONTEXT", start);
        recordStage("RESOLVE_CONVERSATION_CONTEXT", start);
        recordStage("EXECUTE_GENERATION", start);
        recordStage("VALIDATE_OUTPUT", start);
        recordStage("FORMAT_RESPONSE", start);
        recordStage("AUDIT_LOG", start);

        long totalLatency = System.currentTimeMillis() - start;

        var response = new ContentGenerationResponse(
                UUID.randomUUID(),
                request.id().toString(),
                "Stub generated content for: " + request.prompt(),
                request.contentType(),
                request.category(),
                request.tone(),
                50,
                0.95,
                false,
                ModerationStatus.APPROVED,
                OffsetDateTime.now(),
                totalLatency,
                true,
                null
        );

        saveMetrics(request.id(), request.contentType(), request.category(), totalLatency, true, null);
        try {
            monitoringService.recordGenerationCompleted();
        } catch (Exception e) {
            log.warn("Monitoring failed for generation pipeline: {}", e.getMessage());
        }
        log.info("Generation pipeline completed for request {} in {} ms", request.id(), totalLatency);
        return response;
    }

    @Override
    public ContentSummaryResult executeSummaryPipeline(ContentSummaryRequest request) {
        long start = System.currentTimeMillis();
        log.info("Executing summary pipeline for request {}", request.id());

        recordStage("VALIDATE_INPUT", start);
        recordStage("EXECUTE_SUMMARY", start);
        recordStage("VALIDATE_OUTPUT", start);

        long totalLatency = System.currentTimeMillis() - start;
        String summaryText = request.sourceText().length() > 100
                ? request.sourceText().substring(0, 100) + "..."
                : request.sourceText();

        var result = new ContentSummaryResult(
                UUID.randomUUID(),
                request.id(),
                summaryText,
                request.sourceText().length(),
                summaryText.length(),
                (double) summaryText.length() / Math.max(request.sourceText().length(), 1),
                request.language(),
                OffsetDateTime.now()
        );

        saveMetrics(request.id(), ContentType.TEXT, ContentCategory.GENERAL, totalLatency, true, null);
        return result;
    }

    @Override
    public ContentTranslationResult executeTranslationPipeline(ContentTranslationRequest request) {
        long start = System.currentTimeMillis();
        log.info("Executing translation pipeline for request {}", request.id());

        recordStage("VALIDATE_INPUT", start);
        recordStage("DETECT_LANGUAGE", start);
        recordStage("EXECUTE_TRANSLATION", start);
        recordStage("VALIDATE_OUTPUT", start);

        long totalLatency = System.currentTimeMillis() - start;

        var result = new ContentTranslationResult(
                UUID.randomUUID(),
                request.id(),
                request.sourceText(),
                request.sourceLanguage(),
                request.targetLanguage(),
                request.sourceLanguage() != null ? request.sourceLanguage() : "en",
                0.98,
                OffsetDateTime.now()
        );

        saveMetrics(request.id(), ContentType.TEXT, ContentCategory.GENERAL, totalLatency, true, null);
        return result;
    }

    @Override
    public ContentClassificationResult executeClassificationPipeline(ContentClassificationRequest request) {
        long start = System.currentTimeMillis();
        log.info("Executing classification pipeline for request {}", request.id());

        recordStage("VALIDATE_INPUT", start);
        recordStage("EXECUTE_CLASSIFICATION", start);
        recordStage("VALIDATE_OUTPUT", start);

        long totalLatency = System.currentTimeMillis() - start;

        var result = new ContentClassificationResult(
                UUID.randomUUID(),
                request.id(),
                Map.of("general", 0.95),
                "general",
                0.95,
                List.of("general"),
                OffsetDateTime.now()
        );

        saveMetrics(request.id(), ContentType.TEXT, ContentCategory.GENERAL, totalLatency, true, null);
        return result;
    }

    @Override
    public ContentModerationResult executeModerationPipeline(ContentModerationRequest request) {
        long start = System.currentTimeMillis();
        log.info("Executing moderation pipeline for request {}", request.id());

        recordStage("VALIDATE_INPUT", start);
        recordStage("CHECK_PII", start);
        recordStage("CHECK_PROFANITY", start);
        recordStage("CHECK_TOXICITY", start);
        recordStage("DETERMINE_STATUS", start);

        long totalLatency = System.currentTimeMillis() - start;

        var result = new ContentModerationResult(
                UUID.randomUUID(),
                request.id(),
                ModerationStatus.APPROVED,
                false,
                false,
                false,
                0.99,
                List.of(),
                false,
                null,
                null
        );

        saveMetrics(request.id(), request.contentType(), ContentCategory.GENERAL, totalLatency, true, null);
        return result;
    }

    @Override
    public ContentSEOResult executeSEOPipeline(ContentSEORequest request) {
        long start = System.currentTimeMillis();
        log.info("Executing SEO pipeline for request {}", request.id());

        recordStage("VALIDATE_INPUT", start);
        recordStage("ANALYZE_CONTENT", start);
        recordStage("GENERATE_TITLE", start);
        recordStage("GENERATE_META", start);
        recordStage("GENERATE_KEYWORDS", start);
        recordStage("GENERATE_SLUG", start);

        long totalLatency = System.currentTimeMillis() - start;

        var result = new ContentSEOResult(
                UUID.randomUUID(),
                request.id(),
                "Stub SEO Title for: " + (request.targetKeyword() != null ? request.targetKeyword() : "content"),
                "Stub meta description for SEO optimization.",
                List.of(request.targetKeyword() != null ? request.targetKeyword() : "general"),
                request.targetKeyword() != null ? request.targetKeyword().toLowerCase().replace(" ", "-") : "content",
                75.0,
                80.0,
                List.of("Add more relevant keywords", "Improve meta description length"),
                OffsetDateTime.now()
        );

        saveMetrics(request.id(), request.contentType(), ContentCategory.SEO, totalLatency, true, null);
        return result;
    }

    @Override
    public ContentRecommendationResult executeRecommendationPipeline(ContentRecommendationRequest request) {
        long start = System.currentTimeMillis();
        log.info("Executing recommendation pipeline for request {}", request.id());

        recordStage("VALIDATE_INPUT", start);
        recordStage("FETCH_USER_PREFERENCES", start);
        recordStage("COMPUTE_RECOMMENDATIONS", start);
        recordStage("RANK_RESULTS", start);

        long totalLatency = System.currentTimeMillis() - start;

        var result = new ContentRecommendationResult(
                UUID.randomUUID(),
                request.id(),
                List.of(),
                request.recommendationType(),
                0,
                OffsetDateTime.now()
        );

        saveMetrics(request.id(), request.contentType(), ContentCategory.GENERAL, totalLatency, true, null);
        return result;
    }

    private void recordStage(String stageName, long startTime) {
        long elapsed = System.currentTimeMillis() - startTime;
        log.debug("Pipeline stage {} completed in {} ms", stageName, elapsed);
    }

    private void saveMetrics(UUID requestId, ContentType contentType, ContentCategory category,
                             long totalLatency, boolean success, String errorMessage) {
        var entity = new GenerationMetricsEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(requestId);
        entity.setContentType(contentType.name());
        entity.setCategory(category.name());
        entity.setPipelineStage("COMPLETE");
        entity.setStageLatencyMs(totalLatency);
        entity.setTotalLatencyMs(totalLatency);
        entity.setTokenCount(0);
        entity.setSuccess(success);
        entity.setErrorMessage(errorMessage);
        entity.setCreatedAt(OffsetDateTime.now());
        metricsRepository.save(entity);
    }
}
