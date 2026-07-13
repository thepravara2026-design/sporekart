package com.sporekart.ai.content.application;

import com.sporekart.ai.content.domain.*;
import com.sporekart.ai.content.infrastructure.monitoring.ContentMonitoringService;
import com.sporekart.ai.content.infrastructure.persistence.GenerationMetricsEntity;
import com.sporekart.ai.content.infrastructure.persistence.GenerationMetricsRepository;
import com.sporekart.ai.core.domain.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentPipelineExecutorImplTest {

    @Mock private GenerationMetricsRepository metricsRepository;
    @Mock private ContentMonitoringService monitoringService;

    private ContentPipelineExecutorImpl executor;

    @BeforeEach
    void setUp() {
        executor = new ContentPipelineExecutorImpl(metricsRepository, monitoringService);
    }

    @Test
    void shouldExecuteGenerationPipeline() {
        var request = new ContentGenerationRequest(UUID.randomUUID(), "req1", "Write content",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                "en", 500, null, null, UUID.randomUUID());

        var response = executor.executeGenerationPipeline(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("Stub generated content for: " + request.prompt(), response.content());
        assertEquals(ContentType.TEXT, response.contentType());
        assertEquals(ContentCategory.BLOG, response.category());
        assertEquals(ContentTone.PROFESSIONAL, response.tone());
        assertTrue(response.success());
        assertEquals(ModerationStatus.APPROVED, response.moderationStatus());
        verify(metricsRepository).save(any(GenerationMetricsEntity.class));
        verify(monitoringService).recordGenerationCompleted();
    }

    @Test
    void shouldExecuteSummaryPipeline() {
        var request = new ContentSummaryRequest(UUID.randomUUID(),
                "This is a long text that should be summarized by the pipeline executor service",
                50, "en", true);

        var result = executor.executeSummaryPipeline(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertTrue(result.summaryLength() <= result.originalLength());
        assertTrue(result.compressionRatio() > 0);
        assertEquals("en", result.language());
        verify(metricsRepository).save(any(GenerationMetricsEntity.class));
    }

    @Test
    void shouldExecuteSummaryPipelineWithShortText() {
        var request = new ContentSummaryRequest(UUID.randomUUID(), "Short text", 50, "fr", false);

        var result = executor.executeSummaryPipeline(request);

        assertNotNull(result);
        assertEquals("Short text", result.summary());
        assertEquals("fr", result.language());
    }

    @Test
    void shouldExecuteTranslationPipeline() {
        var request = new ContentTranslationRequest(UUID.randomUUID(), "Hello", "en", "fr", true, null);

        var result = executor.executeTranslationPipeline(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals("Hello", result.translatedText());
        assertEquals("fr", result.targetLanguage());
        assertEquals("en", result.sourceLanguage());
        assertTrue(result.confidenceScore() > 0);
        verify(metricsRepository).save(any(GenerationMetricsEntity.class));
    }

    @Test
    void shouldExecuteTranslationPipelineWithNullSourceLanguage() {
        var request = new ContentTranslationRequest(UUID.randomUUID(), "Hello", null, "es", false, null);

        var result = executor.executeTranslationPipeline(request);

        assertNotNull(result);
        assertEquals("es", result.targetLanguage());
        assertEquals("en", result.detectedLanguage());
    }

    @Test
    void shouldExecuteClassificationPipeline() {
        var request = new ContentClassificationRequest(UUID.randomUUID(), "Tech content", null, 3);

        var result = executor.executeClassificationPipeline(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals("general", result.primaryCategory());
        assertTrue(result.confidenceScore() > 0);
        assertFalse(result.classifications().isEmpty());
        assertFalse(result.keywords().isEmpty());
        verify(metricsRepository).save(any(GenerationMetricsEntity.class));
    }

    @Test
    void shouldExecuteModerationPipeline() {
        var request = new ContentModerationRequest(UUID.randomUUID(), "Safe content",
                ContentType.TEXT, true, true, true);

        var result = executor.executeModerationPipeline(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(ModerationStatus.APPROVED, result.status());
        assertFalse(result.containsPii());
        assertFalse(result.containsProfanity());
        assertFalse(result.isToxic());
        assertTrue(result.confidenceScore() > 0);
        assertTrue(result.flags().isEmpty());
        verify(metricsRepository).save(any(GenerationMetricsEntity.class));
    }

    @Test
    void shouldExecuteSEOPipeline() {
        var request = new ContentSEORequest(UUID.randomUUID(), "Content for SEO", "seo-keyword",
                "developers", ContentType.TEXT, "en");

        var result = executor.executeSEOPipeline(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertTrue(result.title().contains("seo-keyword"));
        assertFalse(result.metaDescription().isEmpty());
        assertFalse(result.keywords().isEmpty());
        assertEquals("seo-keyword", result.slug());
        assertTrue(result.seoScore() > 0);
        assertTrue(result.readabilityScore() > 0);
        assertFalse(result.suggestions().isEmpty());
        verify(metricsRepository).save(any(GenerationMetricsEntity.class));
    }

    @Test
    void shouldExecuteSEOPipelineWithNullKeyword() {
        var request = new ContentSEORequest(UUID.randomUUID(), "Content", null,
                null, ContentType.TEXT, "en");

        var result = executor.executeSEOPipeline(request);

        assertNotNull(result);
        assertEquals("content", result.slug());
        assertTrue(result.keywords().contains("general"));
    }

    @Test
    void shouldExecuteRecommendationPipeline() {
        var request = new ContentRecommendationRequest(UUID.randomUUID(), "ctx1", UUID.randomUUID(),
                RecommendationType.PERSONALIZED, ContentType.TEXT, 10, null);

        var result = executor.executeRecommendationPipeline(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(RecommendationType.PERSONALIZED, result.type());
        assertTrue(result.recommendations().isEmpty());
        assertEquals(0, result.totalResults());
        verify(metricsRepository).save(any(GenerationMetricsEntity.class));
    }

    @Test
    void shouldSaveMetricsEvenWhenMonitoringFails() {
        doThrow(new RuntimeException("Monitoring down")).when(monitoringService).recordGenerationCompleted();
        var request = new ContentGenerationRequest(UUID.randomUUID(), "req1", "Write",
                ContentType.TEXT, ContentCategory.GENERAL, ContentTone.NEUTRAL,
                null, 100, null, null, UUID.randomUUID());

        var response = executor.executeGenerationPipeline(request);

        assertNotNull(response);
        verify(metricsRepository).save(any(GenerationMetricsEntity.class));
    }
}
