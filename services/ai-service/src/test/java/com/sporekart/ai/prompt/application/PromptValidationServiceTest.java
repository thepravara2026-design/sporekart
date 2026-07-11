package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptValidationServiceTest {

    @Mock
    private PromptVariableRepository variableRepository;

    private PromptValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new PromptValidationService(variableRepository);
    }

    @Test
    void shouldValidateValidTemplate() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setName("test-prompt");
        template.setTemplateText("Hello {{name}}, welcome!");
        assertDoesNotThrow(() -> validationService.validateTemplate(template));
    }

    @Test
    void shouldRejectBlankName() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setName("");
        template.setTemplateText("Hello");
        assertThrows(PromptValidationException.class, () -> validationService.validateTemplate(template));
    }

    @Test
    void shouldRejectBlankTemplateText() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setName("test");
        template.setTemplateText("");
        assertThrows(PromptValidationException.class, () -> validationService.validateTemplate(template));
    }

    @Test
    void shouldRejectInjectionInTemplate() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setName("test");
        template.setTemplateText("Hello {{user}}, <script>alert('xss')</script>");
        assertDoesNotThrow(() -> validationService.validateTemplate(template));
    }

    @Test
    void shouldRejectUnresolvedVariablesDuringRender() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());
        template.setTemplateText("Hello {{name}}, your {{role}} is active");

        when(variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(any()))
                .thenReturn(List.of());

        assertThrows(PromptValidationException.class,
                () -> validationService.validateRenderVariables(template, Map.of("name", "John")));
    }

    @Test
    void shouldAcceptAllVariablesResolved() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());
        template.setTemplateText("Hello {{name}}");

        when(variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(any()))
                .thenReturn(List.of());

        assertDoesNotThrow(() -> validationService.validateRenderVariables(template, Map.of("name", "John")));
    }

    @Test
    void shouldValidateRequiredVariables() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());

        PromptVariableEntity var = new PromptVariableEntity();
        var.setName("customerName");
        var.setRequired(true);

        when(variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(any()))
                .thenReturn(List.of(var));

        assertThrows(PromptValidationException.class,
                () -> validationService.validateRenderVariables(template, Map.of()));
    }

    @Test
    void shouldDetectInjectionPatterns() {
        assertTrue(validationService.containsInjection("{{malicious}} {{nested}}"));
        assertFalse(validationService.containsInjection("Hello {{name}}"));
    }

    @Test
    void shouldValidatePayloadSize() {
        StringBuilder large = new StringBuilder();
        for (int i = 0; i < 25001; i++) large.append("data");
        assertThrows(PromptValidationException.class,
                () -> validationService.validatePayloadSize(Map.of("data", large.toString())));
    }

    @Test
    void shouldAcceptNormalPayload() {
        assertDoesNotThrow(() -> validationService.validatePayloadSize(Map.of("name", "John")));
    }

    @Test
    void shouldHandleNullVariables() {
        assertDoesNotThrow(() -> validationService.validatePayloadSize(null));
    }

    @Test
    void shouldValidateRegexPattern() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());

        PromptVariableEntity var = new PromptVariableEntity();
        var.setName("email");
        var.setRequired(true);
        var.setValidationRegex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        when(variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(any()))
                .thenReturn(List.of(var));

        assertDoesNotThrow(() -> validationService.validateRenderVariables(template, Map.of("email", "test@example.com")));
        assertThrows(PromptValidationException.class,
                () -> validationService.validateRenderVariables(template, Map.of("email", "invalid")));
    }
}
