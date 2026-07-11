package com.sporekart.ai.prompt.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.prompt.application.PromptApplicationService;
import com.sporekart.ai.prompt.application.PromptAuditService;
import com.sporekart.ai.prompt.application.PromptCategoryService;
import com.sporekart.ai.prompt.application.PromptImportExportService;
import com.sporekart.ai.prompt.application.PromptLifecycleService;
import com.sporekart.ai.prompt.application.PromptRenderService;
import com.sporekart.ai.prompt.application.PromptSearchService;
import com.sporekart.ai.prompt.application.PromptValidationService;
import com.sporekart.ai.prompt.application.PromptVersionService;
import com.sporekart.ai.prompt.infrastructure.PromptKafkaEventPublisher;
import com.sporekart.ai.prompt.infrastructure.PromptRedisCacheService;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableRepository;
import com.sporekart.ai.prompt.interfaces.rest.PromptController;
import com.sporekart.ai.prompt.interfaces.rest.dto.CreatePromptRequest;
import com.sporekart.ai.prompt.interfaces.rest.dto.RenderPromptRequest;
import com.sporekart.ai.prompt.interfaces.rest.dto.UpdatePromptRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PromptControllerTest {

    @Mock private PromptApplicationService promptService;
    @Mock private PromptCategoryService categoryService;
    @Mock private PromptVersionService versionService;
    @Mock private PromptLifecycleService lifecycleService;
    @Mock private PromptAuditService auditService;
    @Mock private PromptSearchService searchService;
    @Mock private PromptRenderService renderService;
    @Mock private PromptImportExportService importExportService;
    @Mock private PromptRedisCacheService cacheService;
    @Mock private PromptKafkaEventPublisher kafkaPublisher;
    @Mock private PromptVariableRepository variableRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        PromptValidationService validationService = new PromptValidationService(variableRepository);
        PromptRenderService realRenderService = new PromptRenderService(validationService);
        objectMapper = new ObjectMapper();

        if (renderService == null) {
            // Use real render service when mock is null in this context
        }

        PromptController controller = new PromptController(
                promptService, categoryService, versionService, lifecycleService,
                auditService, searchService, realRenderService, importExportService,
                cacheService, kafkaPublisher);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(controller)
                .build();
    }

    @Test
    void shouldListPrompts() throws Exception {
        when(promptService.listTemplates()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/ai/prompts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldGetPromptById() throws Exception {
        UUID id = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(id);
        template.setName("test");
        PromptCategoryEntity cat = new PromptCategoryEntity("Cat", null, null, 0);
        cat.setId(UUID.randomUUID());
        template.setCategory(cat);

        when(promptService.getTemplate(id)).thenReturn(template);
        mockMvc.perform(get("/api/v1/ai/prompts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldCreatePrompt() throws Exception {
        UUID categoryId = UUID.randomUUID();
        CreatePromptRequest request = new CreatePromptRequest(
                categoryId, "test", "desc", "Hello {{name}}", null);

        PromptTemplateEntity saved = new PromptTemplateEntity();
        saved.setId(UUID.randomUUID());
        saved.setName("test");
        PromptCategoryEntity cat = new PromptCategoryEntity("Cat", null, null, 0);
        cat.setId(categoryId);
        saved.setCategory(cat);

        when(promptService.createTemplate(any(), eq("test"), any(), any(), any(), any())).thenReturn(saved);

        mockMvc.perform(post("/api/v1/ai/prompts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldUpdatePrompt() throws Exception {
        UUID id = UUID.randomUUID();
        UpdatePromptRequest request = new UpdatePromptRequest(null, "updated", null, "New text {{var}}");

        PromptTemplateEntity updated = new PromptTemplateEntity();
        updated.setId(id);
        updated.setName("updated");
        PromptCategoryEntity cat = new PromptCategoryEntity("Cat", null, null, 0);
        cat.setId(UUID.randomUUID());
        updated.setCategory(cat);

        when(promptService.updateTemplate(eq(id), any(), eq("updated"), any(), eq("New text {{var}}"), any()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/v1/ai/prompts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("updated"));
    }

    @Test
    void shouldDeletePrompt() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/api/v1/ai/prompts/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRenderPrompt() throws Exception {
        RenderPromptRequest request = new RenderPromptRequest("Hello {{name}}!", java.util.Map.of("name", "John"));

        mockMvc.perform(post("/api/v1/ai/prompts/render")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.renderedText").value("Hello John!"));
    }

    @Test
    void shouldRenderPromptWithUnresolvedVariables() throws Exception {
        RenderPromptRequest request = new RenderPromptRequest("Hello {{name}}!", java.util.Map.of());

        mockMvc.perform(post("/api/v1/ai/prompts/render")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldListCategories() throws Exception {
        when(categoryService.listAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/ai/prompts/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldGetHistory() throws Exception {
        org.springframework.data.domain.Page<com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity> emptyPage =
                new org.springframework.data.domain.PageImpl<>(List.of());
        when(auditService.getAllHistory(0, 50)).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/ai/prompts/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldPublishPrompt() throws Exception {
        UUID id = UUID.randomUUID();
        PromptTemplateEntity published = new PromptTemplateEntity();
        published.setId(id);
        published.setName("test");
        published.setStatus("PUBLISHED");
        PromptCategoryEntity cat = new PromptCategoryEntity("Cat", null, null, 0);
        cat.setId(UUID.randomUUID());
        published.setCategory(cat);

        when(lifecycleService.publish(eq(id), any())).thenReturn(published);

        mockMvc.perform(post("/api/v1/ai/prompts/{id}/publish", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PUBLISHED"));
    }

    @Test
    void shouldRollbackPrompt() throws Exception {
        UUID id = UUID.randomUUID();
        com.sporekart.ai.prompt.infrastructure.persistence.PromptVersionEntity version =
                new com.sporekart.ai.prompt.infrastructure.persistence.PromptVersionEntity();
        version.setId(UUID.randomUUID());
        version.setVersionNumber(3);
        version.setStatus("PUBLISHED");
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(id);
        version.setTemplate(template);

        when(versionService.rollback(eq(id), eq(1), any())).thenReturn(version);

        mockMvc.perform(post("/api/v1/ai/prompts/{id}/rollback?version=1", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.versionNumber").value(3));
    }
}
