package com.sporekart.ai.prompt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptImportExportServiceTest {

    @Mock
    private PromptCategoryRepository categoryRepository;
    @Mock
    private PromptTemplateRepository templateRepository;
    @Mock
    private PromptVariableRepository variableRepository;
    @Mock
    private PromptAuditRepository auditRepository;

    private PromptImportExportService importExportService;

    @BeforeEach
    void setUp() {
        importExportService = new PromptImportExportService(
                categoryRepository, templateRepository, variableRepository,
                auditRepository, new ObjectMapper());
    }

    @Test
    void shouldExportAllPrompts() {
        PromptCategoryEntity cat = new PromptCategoryEntity("TestCat", "desc", "icon", 1);
        cat.setId(UUID.randomUUID());
        when(categoryRepository.findByIsDeletedFalseOrderByDisplayOrder()).thenReturn(List.of(cat));
        when(templateRepository.findByCategoryIdAndIsDeletedFalse(cat.getId())).thenReturn(List.of());
        String json = importExportService.exportAll();
        assertNotNull(json);
        assertTrue(json.contains("TestCat"));
    }

    @Test
    void shouldImportPrompts() {
        String json = """
                {
                  "version": "1.0",
                  "categories": [
                    {
                      "name": "TestCat",
                      "templates": [
                        {
                          "name": "test-template",
                          "templateText": "Hello {{name}}",
                          "variables": [
                            {"name": "name", "type": "STRING", "required": true}
                          ]
                        }
                      ]
                    }
                  ]
                }
                """;

        when(categoryRepository.findByNameAndIsDeletedFalse("TestCat"))
                .thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenAnswer(i -> {
            PromptCategoryEntity e = i.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });
        when(templateRepository.existsByNameAndCategoryIdAndIsDeletedFalse(any(), any())).thenReturn(false);
        when(templateRepository.save(any())).thenAnswer(i -> {
            com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity e = i.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });
        when(variableRepository.save(any())).thenReturn(null);
        when(auditRepository.save(any())).thenReturn(null);

        int count = importExportService.importPrompts(json, UUID.randomUUID());
        assertEquals(1, count);
    }

    @Test
    void shouldRejectInvalidJson() {
        assertThrows(PromptValidationException.class,
                () -> importExportService.importPrompts("invalid json", UUID.randomUUID()));
    }
}
