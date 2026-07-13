package com.sporekart.ai.content.interfaces.rest;

import com.sporekart.ai.content.api.ContentClassificationService;
import com.sporekart.ai.content.api.ContentGenerationService;
import com.sporekart.ai.content.api.ContentModerationService;
import com.sporekart.ai.content.api.ContentRecommendationService;
import com.sporekart.ai.content.api.ContentSEOService;
import com.sporekart.ai.content.api.ContentSummaryService;
import com.sporekart.ai.content.api.ContentTemplateService;
import com.sporekart.ai.content.api.ContentTranslationService;
import com.sporekart.ai.content.application.ContentException;
import com.sporekart.ai.content.domain.ContentCategory;
import com.sporekart.ai.content.domain.ContentClassificationRequest;
import com.sporekart.ai.content.domain.ContentGenerationRequest;
import com.sporekart.ai.content.domain.ContentModerationRequest;
import com.sporekart.ai.content.domain.ContentRecommendationRequest;
import com.sporekart.ai.content.domain.ContentSEORequest;
import com.sporekart.ai.content.domain.ContentTemplate;
import com.sporekart.ai.content.domain.ContentTone;
import com.sporekart.ai.content.domain.ContentTranslationRequest;
import com.sporekart.ai.content.domain.RecommendationType;
import com.sporekart.ai.content.infrastructure.kafka.ContentKafkaEventPublisher;
import com.sporekart.ai.content.infrastructure.monitoring.ContentMonitoringService;
import com.sporekart.ai.content.interfaces.rest.dto.ContentClassifyRequest;
import com.sporekart.ai.content.interfaces.rest.dto.ContentClassifyResponse;
import com.sporekart.ai.content.interfaces.rest.dto.ContentGenerateRequest;
import com.sporekart.ai.content.interfaces.rest.dto.ContentGenerateResponse;
import com.sporekart.ai.content.interfaces.rest.dto.ContentHistoryResponse;
import com.sporekart.ai.content.interfaces.rest.dto.ContentModerateRequest;
import com.sporekart.ai.content.interfaces.rest.dto.ContentModerateResponse;
import com.sporekart.ai.content.interfaces.rest.dto.ContentRecommendRequest;
import com.sporekart.ai.content.interfaces.rest.dto.ContentRecommendResponse;
import com.sporekart.ai.content.interfaces.rest.dto.ContentSEOInput;
import com.sporekart.ai.content.interfaces.rest.dto.ContentSEOOutput;
import com.sporekart.ai.content.interfaces.rest.dto.ContentSummaryInput;
import com.sporekart.ai.content.interfaces.rest.dto.ContentSummaryOutput;
import com.sporekart.ai.content.interfaces.rest.dto.ContentTemplateResponse;
import com.sporekart.ai.content.interfaces.rest.dto.ContentTranslateRequest;
import com.sporekart.ai.content.interfaces.rest.dto.ContentTranslateResponse;
import com.sporekart.ai.core.domain.ContentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/content")
@Tag(name = "Content API", description = "Enterprise AI content management endpoints")
public class ContentController {

    private static final Logger log = LoggerFactory.getLogger(ContentController.class);

    private final ContentGenerationService generationService;
    private final ContentSummaryService summaryService;
    private final ContentTranslationService translationService;
    private final ContentClassificationService classificationService;
    private final ContentModerationService moderationService;
    private final ContentSEOService seoService;
    private final ContentRecommendationService recommendationService;
    private final ContentTemplateService templateService;
    private final ContentMonitoringService monitoringService;
    private final ContentKafkaEventPublisher kafkaPublisher;

    public ContentController(ContentGenerationService generationService,
                             ContentSummaryService summaryService,
                             ContentTranslationService translationService,
                             ContentClassificationService classificationService,
                             ContentModerationService moderationService,
                             ContentSEOService seoService,
                             ContentRecommendationService recommendationService,
                             ContentTemplateService templateService,
                             ContentMonitoringService monitoringService,
                             ContentKafkaEventPublisher kafkaPublisher) {
        this.generationService = generationService;
        this.summaryService = summaryService;
        this.translationService = translationService;
        this.classificationService = classificationService;
        this.moderationService = moderationService;
        this.seoService = seoService;
        this.recommendationService = recommendationService;
        this.templateService = templateService;
        this.monitoringService = monitoringService;
        this.kafkaPublisher = kafkaPublisher;
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate content from a prompt")
    public ResponseEntity<ContentGenerateResponse> generateContent(@Valid @RequestBody ContentGenerateRequest request) {
        var domainRequest = new ContentGenerationRequest(
                null, null, request.prompt(),
                ContentType.valueOf(request.contentType().toUpperCase()),
                request.category() != null ? ContentCategory.valueOf(request.category().toUpperCase()) : null,
                request.tone() != null ? ContentTone.valueOf(request.tone().toUpperCase()) : null,
                request.targetLanguage(), request.maxLength(),
                request.parameters(), request.templateId(), request.userId());
        var response = monitoringService.recordGenerationLatency(() ->
                generationService.generate(domainRequest));
        kafkaPublisher.publishContentGenerationStarted(response.id(), response.contentType().name());
        log.info("Generated content: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentGenerateResponse.from(response));
    }

    @PostMapping("/summarize")
    @Operation(summary = "Summarize content")
    public ResponseEntity<ContentSummaryOutput> summarizeContent(@Valid @RequestBody ContentSummaryInput request) {
        var domainRequest = new com.sporekart.ai.content.domain.ContentSummaryRequest(
                null, request.text(), request.maxLength(),
                request.language(), request.preserveKeyPoints());
        var result = monitoringService.recordSummaryLatency(() ->
                summaryService.summarize(domainRequest));
        kafkaPublisher.publishSummaryGenerated(result.id(), result.id());
        log.info("Summarized content: {}", result.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentSummaryOutput.from(result));
    }

    @PostMapping("/translate")
    @Operation(summary = "Translate content to a target language")
    public ResponseEntity<ContentTranslateResponse> translateContent(@Valid @RequestBody ContentTranslateRequest request) {
        var domainRequest = new ContentTranslationRequest(
                null, request.text(), request.sourceLanguage(),
                request.targetLanguage(), request.preserveFormatting(), null);
        var result = monitoringService.recordTranslationLatency(() ->
                translationService.translate(domainRequest));
        kafkaPublisher.publishTranslationCompleted(result.id(), result.targetLanguage());
        log.info("Translated content: {} to {}", result.id(), result.targetLanguage());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentTranslateResponse.from(result));
    }

    @PostMapping("/classify")
    @Operation(summary = "Classify content into categories")
    public ResponseEntity<ContentClassifyResponse> classifyContent(@Valid @RequestBody ContentClassifyRequest request) {
        var domainRequest = new ContentClassificationRequest(
                null, request.text(), request.categories(), request.maxCategories());
        var result = monitoringService.recordClassificationLatency(() ->
                classificationService.classify(domainRequest));
        kafkaPublisher.publishClassificationCompleted(result.id(), result.primaryCategory());
        log.info("Classified content: {} as {}", result.id(), result.primaryCategory());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentClassifyResponse.from(result));
    }

    @PostMapping("/recommend")
    @Operation(summary = "Get content recommendations")
    public ResponseEntity<ContentRecommendResponse> recommendContent(@Valid @RequestBody ContentRecommendRequest request) {
        var domainRequest = new ContentRecommendationRequest(
                null, request.contextId(), request.userId(),
                request.recommendationType() != null
                        ? RecommendationType.valueOf(request.recommendationType().toUpperCase())
                        : null,
                request.contentType() != null
                        ? ContentType.valueOf(request.contentType().toUpperCase())
                        : null,
                request.maxResults(), request.filters());
        var result = recommendationService.getRecommendations(domainRequest);
        kafkaPublisher.publishRecommendationGenerated(result.id(), result.id());
        log.info("Generated recommendations: {}", result.id());
        return ResponseEntity.ok(ContentRecommendResponse.from(result));
    }

    @PostMapping("/moderate")
    @Operation(summary = "Moderate content for policy violations")
    public ResponseEntity<ContentModerateResponse> moderateContent(@Valid @RequestBody ContentModerateRequest request) {
        var domainRequest = new ContentModerationRequest(
                null, request.content(),
                request.contentType() != null
                        ? ContentType.valueOf(request.contentType().toUpperCase())
                        : null,
                request.checkPii(), request.checkProfanity(), request.checkToxicity());
        var result = monitoringService.recordModerationLatency(() ->
                moderationService.moderate(domainRequest));
        kafkaPublisher.publishContentModerated(result.id(),
                result.status() == com.sporekart.ai.content.domain.ModerationStatus.APPROVED,
                result.flags().isEmpty() ? "approved" : String.join(", ", result.flags()));
        log.info("Moderated content: {} status={}", result.id(), result.status());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentModerateResponse.from(result));
    }

    @PostMapping("/seo")
    @Operation(summary = "Generate SEO metadata for content")
    public ResponseEntity<ContentSEOOutput> generateSEO(@Valid @RequestBody ContentSEOInput request) {
        var domainRequest = new com.sporekart.ai.content.domain.ContentSEORequest(
                null, request.content(), request.targetKeyword(),
                request.targetAudience(),
                request.contentType() != null
                        ? ContentType.valueOf(request.contentType().toUpperCase())
                        : null,
                request.language());
        var result = monitoringService.recordSEOLatency(() ->
                seoService.generateSEO(domainRequest));
        log.info("Generated SEO metadata: {}", result.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContentSEOOutput.from(result));
    }

    @GetMapping("/history")
    @Operation(summary = "List content generation history")
    public ResponseEntity<List<ContentHistoryResponse>> listHistory(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var history = generationService.listHistory(userId, page, size)
                .stream()
                .map(ContentHistoryResponse::from)
                .toList();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/templates")
    @Operation(summary = "List content templates")
    public ResponseEntity<List<ContentTemplateResponse>> listTemplates(
            @RequestParam(required = false) String category) {
        var cat = category != null ? ContentCategory.valueOf(category.toUpperCase()) : null;
        var templates = templateService.listTemplates(cat)
                .stream()
                .map(ContentTemplateResponse::from)
                .toList();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check for content module")
    public ResponseEntity<ContentMonitoringService.HealthStatus> health() {
        return ResponseEntity.ok(monitoringService.checkHealth());
    }
}
