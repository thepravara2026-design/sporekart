package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptApplicationServiceTest {

    @Mock
    private PromptTemplateRepository templateRepository;
    @Mock
    private PromptCategoryRepository categoryRepository;
    @Mock
    private PromptVariableRepository variableRepository;
    @Mock
    private PromptAuditRepository auditRepository;
    @Mock
    private PromptValidationService validationService;
    @Mock
    private PromptRenderService renderService;
    @Mock
    private PromptVersionService versionService;
    @Mock
    private PromptLifecycleService lifecycleService;

    private PromptApplicationService applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new PromptApplicationService(
                templateRepository, categoryRepository, variableRepository, auditRepository,
                validationService, renderService, versionService, lifecycleService);
    }

    @Test
    void shouldCreateTemplate() {
        UUID categoryId = UUID.randomUUID();
        PromptCategoryEntity category = new PromptCategoryEntity("Test", null, null, 0);
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(templateRepository.existsByNameAndCategoryIdAndIsDeletedFalse("test", categoryId)).thenReturn(false);
        when(templateRepository.save(any())).thenAnswer(i -> {
            PromptTemplateEntity e = i.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });
        when(auditRepository.save(any())).thenReturn(null);

        PromptTemplateEntity created = applicationService.createTemplate(
                categoryId, "test", "desc", "Hello {{name}}", null, UUID.randomUUID());
        assertNotNull(created);
        assertEquals("test", created.getName());
    }

    @Test
    void shouldGetTemplate() {
        UUID id = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(id);
        template.setName("test");

        when(templateRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(template));
        assertEquals("test", applicationService.getTemplate(id).getName());
    }

    @Test
    void shouldThrowOnMissingTemplate() {
        when(templateRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Optional.empty());
        assertThrows(PromptNotFoundException.class, () -> applicationService.getTemplate(UUID.randomUUID()));
    }

    @Test
    void shouldUpdateTemplate() {
        UUID id = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(id);
        template.setName("old");
        template.setCategory(new PromptCategoryEntity("Cat", null, null, 0));

        when(templateRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptTemplateEntity updated = applicationService.updateTemplate(id, null, "new", null, null, UUID.randomUUID());
        assertEquals("new", updated.getName());
    }

    @Test
    void shouldDeleteTemplate() {
        UUID id = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(id);
        template.setName("test");

        when(templateRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        assertDoesNotThrow(() -> applicationService.deleteTemplate(id, UUID.randomUUID()));
    }

    @Test
    void shouldListTemplates() {
        when(templateRepository.findByIsDeletedFalse()).thenReturn(java.util.List.of());
        assertNotNull(applicationService.listTemplates());
    }
}
