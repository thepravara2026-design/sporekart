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
class PromptRenderServiceTest {

    @Mock
    private PromptVariableRepository variableRepository;

    private PromptRenderService renderService;
    private PromptValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new PromptValidationService(variableRepository);
        renderService = new PromptRenderService(validationService);
    }

    @Test
    void shouldRenderSimpleTemplate() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());
        template.setTemplateText("Hello {{name}}!");

        when(variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(any()))
                .thenReturn(List.of());

        String result = renderService.render(template, Map.of("name", "John"));
        assertEquals("Hello John!", result);
    }

    @Test
    void shouldRejectUnresolvedVariables() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());
        template.setTemplateText("Hello {{name}}!");

        when(variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(any()))
                .thenReturn(List.of());

        assertThrows(PromptValidationException.class,
                () -> renderService.render(template, Map.of()));
    }

    @Test
    void shouldRenderRawTemplate() {
        String result = renderService.renderRaw("Welcome {{user}}!", Map.of("user", "admin"));
        assertEquals("Welcome admin!", result);
    }

    @Test
    void shouldRejectUnresolvedInRawRender() {
        assertThrows(PromptRenderException.class,
                () -> renderService.renderRaw("Hello {{name}}!", Map.of()));
    }

    @Test
    void shouldRenderWithDefaults() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());
        template.setTemplateText("Hello {{name}} from {{city}}!");

        PromptVariableEntity varCity = new PromptVariableEntity();
        varCity.setName("city");
        varCity.setDefaultValue("New York");

        PromptVariableEntity varName = new PromptVariableEntity();
        varName.setName("name");
        varName.setRequired(true);

        String result = renderService.renderWithDefaults(template, Map.of("name", "John"),
                List.of(varCity, varName));
        assertEquals("Hello John from New York!", result);
    }

    @Test
    void shouldEscapeSpecialCharacters() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());
        template.setTemplateText("Message: {{text}}");

        when(variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(any()))
                .thenReturn(List.of());

        String result = renderService.render(template, Map.of("text", "Line1\nLine2\"quote\""));
        assertTrue(result.contains("\\n"));
        assertTrue(result.contains("\\\""));
    }

    @Test
    void shouldHandleMultipleVariables() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());
        template.setTemplateText("{{greeting}} {{name}}, your {{role}} is confirmed");

        when(variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(any()))
                .thenReturn(List.of());

        String result = renderService.render(template,
                Map.of("greeting", "Welcome", "name", "Alice", "role", "admin"));
        assertEquals("Welcome Alice, your admin is confirmed", result);
    }

    @Test
    void shouldRejectNullTemplateText() {
        assertThrows(PromptRenderException.class,
                () -> renderService.renderRaw(null, Map.of()));
    }

    @Test
    void shouldRenderWithNoVariablesWhenNoneInTemplate() {
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());
        template.setTemplateText("Static text without variables");

        when(variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(any()))
                .thenReturn(List.of());

        String result = renderService.render(template, Map.of());
        assertEquals("Static text without variables", result);
    }
}
