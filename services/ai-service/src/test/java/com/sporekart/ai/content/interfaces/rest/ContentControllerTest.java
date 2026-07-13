package com.sporekart.ai.content.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.ai.content.api.ContentClassificationService;
import com.sporekart.ai.content.api.ContentGenerationService;
import com.sporekart.ai.content.api.ContentModerationService;
import com.sporekart.ai.content.api.ContentRecommendationService;
import com.sporekart.ai.content.api.ContentSEOService;
import com.sporekart.ai.content.api.ContentSummaryService;
import com.sporekart.ai.content.api.ContentTemplateService;
import com.sporekart.ai.content.api.ContentTranslationService;
import com.sporekart.ai.content.application.ContentException;
import com.sporekart.ai.content.domain.*;
import com.sporekart.ai.content.infrastructure.kafka.ContentKafkaEventPublisher;
import com.sporekart.ai.content.infrastructure.monitoring.ContentMonitoringService;
import com.sporekart.ai.content.interfaces.rest.dto.*;
import com.sporekart.ai.core.domain.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ContentControllerTest {

    @Mock private ContentGenerationService generationService;
    @Mock private ContentSummaryService summaryService;
    @Mock private ContentTranslationService translationService;
    @Mock private ContentClassificationService classificationService;
    @Mock private ContentModerationService moderationService;
    @Mock private ContentSEOService seoService;
    @Mock private ContentRecommendationService recommendationService;
    @Mock private ContentTemplateService templateService;
    @Mock private ContentMonitoringService monitoringService;
    @Mock private ContentKafkaEventPublisher kafkaPublisher;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(ContentException.class)
        @ResponseBody
        public ResponseEntity<String> handleContentException(ContentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseBody
        public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        var controller = new ContentController(generationService, summaryService, translationService,
                classificationService, moderationService, seoService, recommendationService,
                templateService, monitoringService, kafkaPublisher);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldGenerateContent() throws Exception {
        var response = new ContentGenerationResponse(UUID.randomUUID(), "req1", "Generated content",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.PROFESSIONAL,
                150, 0.95, false, ModerationStatus.APPROVED,
                OffsetDateTime.now(), 100L, true, null);
        when(monitoringService.recordGenerationLatency(any())).thenReturn(response);

        var request = new ContentGenerateRequest("Write a blog post", "TEXT", "BLOG",
                "PROFESSIONAL", null, 500, null, null, UUID.randomUUID());
        mockMvc.perform(post("/api/v1/content/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contentType").value("TEXT"));
    }

    @Test
    void shouldReturn400WhenGeneratingWithInvalidContentType() throws Exception {
        var request = Map.<String, Object>of(
                "prompt", "Test", "contentType", "INVALID_TYPE");
        mockMvc.perform(post("/api/v1/content/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenGeneratingWithBlankPrompt() throws Exception {
        var request = Map.<String, Object>of(
                "prompt", "", "contentType", "TEXT");
        mockMvc.perform(post("/api/v1/content/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSummarizeContent() throws Exception {
        var result = new ContentSummaryResult(UUID.randomUUID(), UUID.randomUUID(),
                "Summary text", 500, 50, 0.1, "en", OffsetDateTime.now());
        when(monitoringService.recordSummaryLatency(any())).thenReturn(result);

        var request = new ContentSummaryInput("Long text to summarize", 100, "en", true);
        mockMvc.perform(post("/api/v1/content/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.summary").value("Summary text"));
    }

    @Test
    void shouldReturn400WhenSummarizingWithBlankText() throws Exception {
        var request = Map.<String, Object>of("text", "", "maxLength", 100);
        mockMvc.perform(post("/api/v1/content/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldTranslateContent() throws Exception {
        var result = new ContentTranslationResult(UUID.randomUUID(), UUID.randomUUID(),
                "Translated text", "en", "fr", "en", 0.98, OffsetDateTime.now());
        when(monitoringService.recordTranslationLatency(any())).thenReturn(result);

        var request = new ContentTranslateRequest("Hello", "fr", "en", false);
        mockMvc.perform(post("/api/v1/content/translate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.targetLanguage").value("fr"));
    }

    @Test
    void shouldReturn400WhenTranslatingWithBlankText() throws Exception {
        var request = Map.<String, Object>of("text", "", "targetLanguage", "fr");
        mockMvc.perform(post("/api/v1/content/translate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenTranslatingWithBlankTargetLanguage() throws Exception {
        var request = Map.<String, Object>of("text", "Hello", "targetLanguage", "");
        mockMvc.perform(post("/api/v1/content/translate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldClassifyContent() throws Exception {
        var result = new ContentClassificationResult(UUID.randomUUID(), UUID.randomUUID(),
                Map.of("tech", 0.95), "tech", 0.95, List.of("tech"), OffsetDateTime.now());
        when(monitoringService.recordClassificationLatency(any())).thenReturn(result);

        var request = new ContentClassifyRequest("Tech article content", List.of("tech", "science"), 3);
        mockMvc.perform(post("/api/v1/content/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.primaryCategory").value("tech"));
    }

    @Test
    void shouldReturn400WhenClassifyingWithBlankText() throws Exception {
        var request = Map.<String, Object>of("text", "", "categories", List.of("tech"));
        mockMvc.perform(post("/api/v1/content/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetRecommendations() throws Exception {
        var result = new ContentRecommendationResult(UUID.randomUUID(), UUID.randomUUID(),
                List.of(), RecommendationType.PERSONALIZED, 0, OffsetDateTime.now());
        when(recommendationService.getRecommendations(any())).thenReturn(result);

        var request = new ContentRecommendRequest("ctx1", UUID.randomUUID(),
                "PERSONALIZED", "TEXT", 10, Map.of());
        mockMvc.perform(post("/api/v1/content/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("PERSONALIZED"));
    }

    @Test
    void shouldModerateContent() throws Exception {
        var result = new ContentModerationResult(UUID.randomUUID(), UUID.randomUUID(),
                ModerationStatus.APPROVED, false, false, false, 0.99,
                List.of(), false, null, null);
        when(monitoringService.recordModerationLatency(any())).thenReturn(result);

        var request = new ContentModerateRequest("Safe content", "TEXT", true, true, true);
        mockMvc.perform(post("/api/v1/content/moderate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldReturn400WhenModeratingWithBlankContent() throws Exception {
        var request = Map.<String, Object>of("content", "", "contentType", "TEXT");
        mockMvc.perform(post("/api/v1/content/moderate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGenerateSEO() throws Exception {
        var result = new ContentSEOResult(UUID.randomUUID(), UUID.randomUUID(),
                "SEO Title", "Meta desc", List.of("keyword"), "keyword-slug",
                80.0, 75.0, List.of("Improve density"), OffsetDateTime.now());
        when(monitoringService.recordSEOLatency(any())).thenReturn(result);

        var request = new ContentSEORequest(UUID.randomUUID(), "Content body", "keyword", "developers", ContentType.TEXT, "en");
        mockMvc.perform(post("/api/v1/content/seo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seoScore").value(75.0));
    }

    @Test
    void shouldReturn400WhenSEOWithBlankContent() throws Exception {
        var request = Map.<String, Object>of("content", "", "targetKeyword", "kw");
        mockMvc.perform(post("/api/v1/content/seo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenSEOWithBlankKeyword() throws Exception {
        var request = Map.<String, Object>of("content", "text", "targetKeyword", "");
        mockMvc.perform(post("/api/v1/content/seo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldListHistory() throws Exception {
        var userId = UUID.randomUUID();
        var response = new ContentGenerationResponse(UUID.randomUUID(), "req1", "content",
                ContentType.TEXT, ContentCategory.BLOG, ContentTone.NEUTRAL,
                100, 0.9, false, ModerationStatus.APPROVED,
                OffsetDateTime.now(), 50L, true, null);
        when(generationService.listHistory(userId, 0, 20)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/content/history")
                        .param("userId", userId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contentType").value("TEXT"));
    }

    @Test
    void shouldReturnEmptyHistoryWhenNoneExists() throws Exception {
        var userId = UUID.randomUUID();
        when(generationService.listHistory(userId, 0, 20)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/content/history")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldListTemplates() throws Exception {
        var template = new ContentTemplate(UUID.randomUUID(), "Blog Template", "A blog template",
                ContentCategory.BLOG, ContentType.TEXT, "Content {{var}}",
                List.of("var"), true, 1, UUID.randomUUID(),
                OffsetDateTime.now(), OffsetDateTime.now());
        when(templateService.listTemplates(ContentCategory.BLOG)).thenReturn(List.of(template));

        mockMvc.perform(get("/api/v1/content/templates")
                        .param("category", "BLOG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Blog Template"));
    }

    @Test
    void shouldListTemplatesWithoutFilter() throws Exception {
        var template = new ContentTemplate(UUID.randomUUID(), "Generic Template", "desc",
                ContentCategory.GENERAL, ContentType.TEXT, "Content",
                List.of(), true, 1, UUID.randomUUID(),
                OffsetDateTime.now(), OffsetDateTime.now());
        when(templateService.listTemplates(null)).thenReturn(List.of(template));

        mockMvc.perform(get("/api/v1/content/templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Generic Template"));
    }

    @Test
    void shouldReturnEmptyTemplatesWhenNoneExist() throws Exception {
        when(templateService.listTemplates(ContentCategory.BLOG)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/content/templates")
                        .param("category", "BLOG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldCheckHealth() throws Exception {
        var health = new ContentMonitoringService.HealthStatus("UP");
        when(monitoringService.checkHealth()).thenReturn(health);

        mockMvc.perform(get("/api/v1/content/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        when(monitoringService.recordGenerationLatency(any()))
                .thenThrow(new ContentException("Service error"));

        var request = new ContentGenerateRequest("Write a blog post", "TEXT", "BLOG",
                "PROFESSIONAL", null, 500, null, null, UUID.randomUUID());
        mockMvc.perform(post("/api/v1/content/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldHandleIllegalArgumentException() throws Exception {
        when(monitoringService.recordGenerationLatency(any()))
                .thenThrow(new IllegalArgumentException("Invalid argument"));

        var request = new ContentGenerateRequest("Write a blog post", "TEXT", "BLOG",
                "PROFESSIONAL", null, 500, null, null, UUID.randomUUID());
        mockMvc.perform(post("/api/v1/content/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleRecommendationWithInvalidTypeAsBadRequest() throws Exception {
        var request = Map.<String, Object>of(
                "contextId", "ctx1", "recommendationType", "INVALID_TYPE");
        mockMvc.perform(post("/api/v1/content/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
