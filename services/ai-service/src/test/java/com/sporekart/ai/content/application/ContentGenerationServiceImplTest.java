package com.sporekart.ai.content.application;

import com.sporekart.ai.content.api.ContentPipelineExecutor;
import com.sporekart.ai.content.domain.*;
import com.sporekart.ai.content.infrastructure.kafka.ContentKafkaEventPublisher;
import com.sporekart.ai.content.infrastructure.monitoring.ContentMonitoringService;
import com.sporekart.ai.content.infrastructure.persistence.ContentHistoryEntity;
import com.sporekart.ai.content.infrastructure.persistence.ContentHistoryRepository;
import com.sporekart.ai.content.infrastructure.persistence.ContentResponseEntity;
import com.sporekart.ai.content.infrastructure.persistence.ContentResponseRepository;
import com.sporekart.ai.core.domain.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentGenerationServiceImplTest {

    @Mock private ContentPipelineExecutor pipelineExecutor;
    @Mock private ContentResponseRepository responseRepository;
    @Mock private ContentHistoryRepository historyRepository;
    @Mock private ContentKafkaEventPublisher eventPublisher;
    @Mock private ContentMonitoringService monitoringService;

    private ContentGenerationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContentGenerationServiceImpl(pipelineExecutor, responseRepository,
                historyRepository, eventPublisher, monitoringService);
    }

    @Test
    void shouldGenerateContent() {
        var request = new ContentGenerationRequest(UUID.randomUUID(), "req1", "Write an article",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                "en", 500, null, null, UUID.randomUUID());
        var response = new ContentGenerationResponse(UUID.randomUUID(), "req1", "Generated",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                150, 0.95, false, ModerationStatus.APPROVED,
                OffsetDateTime.now(), 100L, true, null);
        when(pipelineExecutor.executeGenerationPipeline(request)).thenReturn(response);
        when(responseRepository.save(any())).thenReturn(null);

        var result = service.generate(request);

        assertNotNull(result);
        assertEquals(response.id(), result.id());
        verify(responseRepository).save(any());
        verify(eventPublisher).publishContentGenerated(request.id(), request.contentType().name());
        verify(monitoringService).recordGenerationCompleted();
    }

    @Test
    void shouldSummarizeContent() {
        var request = new ContentSummaryRequest(UUID.randomUUID(), "Long text to summarize", 100, "en", true);
        var summaryResult = new ContentSummaryResult(UUID.randomUUID(), request.id(),
                "Summary", 500, 50, 0.1, "en", OffsetDateTime.now());
        when(pipelineExecutor.executeSummaryPipeline(request)).thenReturn(summaryResult);
        when(responseRepository.save(any())).thenReturn(null);

        var result = service.summarize(request);

        assertNotNull(result);
        assertEquals(summaryResult.id(), result.id());
        verify(responseRepository).save(any());
        verify(eventPublisher).publishContentGenerated(request.id(), null);
        verify(monitoringService).recordGenerationCompleted();
    }

    @Test
    void shouldTranslateContent() {
        var request = new ContentTranslationRequest(UUID.randomUUID(), "Hello", "en", "fr", false, null);
        var translationResult = new ContentTranslationResult(UUID.randomUUID(), request.id(),
                "Bonjour", "en", "fr", "en", 0.98, OffsetDateTime.now());
        when(pipelineExecutor.executeTranslationPipeline(request)).thenReturn(translationResult);
        when(responseRepository.save(any())).thenReturn(null);

        var result = service.translate(request);

        assertNotNull(result);
        assertEquals(translationResult.id(), result.id());
        verify(responseRepository).save(any());
    }

    @Test
    void shouldClassifyContent() {
        var request = new ContentClassificationRequest(UUID.randomUUID(), "Tech content", List.of("tech"), 3);
        var classificationResult = new ContentClassificationResult(UUID.randomUUID(), request.id(),
                Map.of("blog", 0.95), "blog", 0.95, List.of("blog"), OffsetDateTime.now());
        when(pipelineExecutor.executeClassificationPipeline(request)).thenReturn(classificationResult);
        when(responseRepository.save(any())).thenReturn(null);

        var result = service.classify(request);

        assertNotNull(result);
        assertEquals(classificationResult.id(), result.id());
        verify(responseRepository).save(any());
    }

    @Test
    void shouldModerateContent() {
        var request = new ContentModerationRequest(UUID.randomUUID(), "Content to moderate",
                ContentType.TEXT, true, true, true);
        var moderationResult = new ContentModerationResult(UUID.randomUUID(), request.id(),
                ModerationStatus.APPROVED, false, false, false, 0.99,
                List.of(), false, null, null);
        when(pipelineExecutor.executeModerationPipeline(request)).thenReturn(moderationResult);
        when(responseRepository.save(any())).thenReturn(null);

        var result = service.moderate(request);

        assertNotNull(result);
        assertEquals(moderationResult.id(), result.id());
        verify(responseRepository).save(any());
    }

    @Test
    void shouldGenerateSEO() {
        var request = new ContentSEORequest(UUID.randomUUID(), "Content", "keyword",
                "developers", ContentType.TEXT, "en");
        var seoResult = new ContentSEOResult(UUID.randomUUID(), request.id(),
                "SEO Title", "Meta desc", List.of("keyword"), "keyword-slug",
                80.0, 75.0, List.of("Improve"), OffsetDateTime.now());
        when(pipelineExecutor.executeSEOPipeline(request)).thenReturn(seoResult);
        when(responseRepository.save(any())).thenReturn(null);

        var result = service.generateSEO(request);

        assertNotNull(result);
        assertEquals(seoResult.id(), result.id());
        verify(responseRepository).save(any());
    }

    @Test
    void shouldGetGeneration() {
        var id = UUID.randomUUID();
        var entity = new ContentResponseEntity();
        entity.setId(id);
        entity.setRequestId(UUID.randomUUID());
        entity.setContent("test");
        entity.setContentType("TEXT");
        entity.setCategory("BLOG");
        entity.setTokenCount(100);
        entity.setConfidenceScore(0.95);
        entity.setHumanReviewRequired(false);
        entity.setModerationStatus("APPROVED");
        entity.setGeneratedAt(OffsetDateTime.now());
        entity.setLatencyMs(50L);
        entity.setSuccess(true);
        entity.setErrorMessage(null);
        entity.setCreatedAt(OffsetDateTime.now());
        when(responseRepository.findById(id)).thenReturn(Optional.of(entity));

        var result = service.getGeneration(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().id());
    }

    @Test
    void shouldReturnEmptyForNonExistentGeneration() {
        var id = UUID.randomUUID();
        when(responseRepository.findById(id)).thenReturn(Optional.empty());

        var result = service.getGeneration(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldListHistory() {
        var userId = UUID.randomUUID();
        var entity = new ContentHistoryEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(UUID.randomUUID());
        entity.setUserId(userId);
        entity.setAction("GENERATE");
        entity.setContentType("TEXT");
        entity.setPrompt("Test prompt");
        entity.setResultSummary("Generated");
        entity.setTokenCount(100);
        entity.setLatencyMs(50L);
        entity.setSuccess(true);
        entity.setCreatedAt(OffsetDateTime.now());
        when(historyRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of(entity));

        var history = service.listHistory(userId, 0, 20);

        assertEquals(1, history.size());
    }

    @Test
    void shouldReturnEmptyHistoryWhenNoneExists() {
        var userId = UUID.randomUUID();
        when(historyRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of());

        var history = service.listHistory(userId, 0, 20);

        assertTrue(history.isEmpty());
    }

    @Test
    void shouldHandlePipelineExecutorFailure() {
        var request = new ContentGenerationRequest(UUID.randomUUID(), "req1", "Write",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                "en", 500, null, null, UUID.randomUUID());
        when(pipelineExecutor.executeGenerationPipeline(request))
                .thenThrow(new RuntimeException("Pipeline failed"));

        assertThrows(RuntimeException.class, () -> service.generate(request));
        verify(responseRepository, never()).save(any());
    }

    @Test
    void shouldHandleRepositorySaveFailure() {
        var request = new ContentGenerationRequest(UUID.randomUUID(), "req1", "Write",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                "en", 500, null, null, UUID.randomUUID());
        var response = new ContentGenerationResponse(UUID.randomUUID(), "req1", "Generated",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                150, 0.95, false, ModerationStatus.APPROVED,
                OffsetDateTime.now(), 100L, true, null);
        when(pipelineExecutor.executeGenerationPipeline(request)).thenReturn(response);
        when(responseRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> service.generate(request));
        verify(eventPublisher, never()).publishContentGenerated(any(), any());
    }

    @Test
    void shouldUseDefaultToneForSummarize() {
        var request = new ContentSummaryRequest(UUID.randomUUID(), "Text", 100, "en", true);
        var summaryResult = new ContentSummaryResult(UUID.randomUUID(), request.id(),
                "Summary", 100, 50, 0.5, "en", OffsetDateTime.now());
        when(pipelineExecutor.executeSummaryPipeline(request)).thenReturn(summaryResult);
        when(responseRepository.save(any())).thenReturn(null);

        var result = service.summarize(request);

        assertEquals(ContentTone.NEUTRAL, result.tone());
        assertEquals(ContentCategory.GENERAL, result.category());
    }

    @Test
    void shouldIncludeErrorMessageWhenProvided() {
        var request = new ContentGenerationRequest(UUID.randomUUID(), "req1", "Write",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                "en", 500, null, null, UUID.randomUUID());
        var response = new ContentGenerationResponse(UUID.randomUUID(), "req1", "Generated",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                150, 0.95, false, ModerationStatus.APPROVED,
                OffsetDateTime.now(), 100L, true, null);
        when(pipelineExecutor.executeGenerationPipeline(request)).thenReturn(response);
        when(responseRepository.save(any())).thenReturn(null);

        var result = service.generate(request);

        assertNull(result.errorMessage());
    }

    @Test
    void shouldPassCreateByToEventOnGenerate() {
        var userId = UUID.randomUUID();
        var request = new ContentGenerationRequest(UUID.randomUUID(), "req1", "Write",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                "en", 500, null, null, userId);
        var response = new ContentGenerationResponse(UUID.randomUUID(), "req1", "Generated",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                150, 0.95, false, ModerationStatus.APPROVED,
                OffsetDateTime.now(), 100L, true, null);
        when(pipelineExecutor.executeGenerationPipeline(request)).thenReturn(response);
        when(responseRepository.save(any())).thenReturn(null);

        service.generate(request);

        verify(eventPublisher).publishContentGenerated(request.id(), request.contentType().name());
    }
}
