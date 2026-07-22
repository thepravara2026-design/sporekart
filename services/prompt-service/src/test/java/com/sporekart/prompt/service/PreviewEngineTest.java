package com.sporekart.prompt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sporekart.prompt.entity.PromptVersionEntity;
import com.sporekart.prompt.repository.PromptVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PreviewEngineTest {

    @Mock
    private PromptVersionRepository versionRepository;

    private PreviewEngine engine;
    private TokenEstimator tokenEstimator;
    private UUID versionId;

    @BeforeEach
    void setUp() {
        tokenEstimator = new TokenEstimator();
        engine = new PreviewEngine(versionRepository, tokenEstimator);
        versionId = UUID.randomUUID();
    }

    @Test
    void shouldRenderSimpleVariables() {
        var version = createVersion("Hello {{customer_name}}, order {{order_number}}");
        when(versionRepository.findById(versionId)).thenReturn(Optional.of(version));

        var result = engine.preview(versionId, Map.of("customer_name", "John", "order_number", "12345"));

        assertThat(result.renderedPrompt()).isEqualTo("Hello John, order 12345");
        assertThat(result.missingVariables()).isEmpty();
    }

    @Test
    void shouldReportMissingVariables() {
        var version = createVersion("Hello {{name}}, your {{item}} is ready");
        when(versionRepository.findById(versionId)).thenReturn(Optional.of(version));

        var result = engine.preview(versionId, Map.of("name", "John"));

        assertThat(result.missingVariables()).contains("item");
        assertThat(result.renderedPrompt()).contains("[MISSING:item]");
    }

    @Test
    void shouldRenderOptionalVariables() {
        var version = createVersion("Hello {{name}}{{?greeting}}");
        when(versionRepository.findById(versionId)).thenReturn(Optional.of(version));

        var result = engine.preview(versionId, Map.of("name", "John"));

        assertThat(result.renderedPrompt()).isEqualTo("Hello John");
    }

    @Test
    void shouldRenderOptionalVariableWhenPresent() {
        var version = createVersion("Hello {{name}}{{?greeting}}");
        when(versionRepository.findById(versionId)).thenReturn(Optional.of(version));

        var result = engine.preview(versionId, Map.of("name", "John", "greeting", "!"));

        assertThat(result.renderedPrompt()).isEqualTo("Hello John!");
    }

    @Test
    void shouldThrowWhenVersionNotFound() {
        when(versionRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> engine.preview(versionId, Map.of()));
    }

    @Test
    void shouldExtractVariables() {
        var vars = engine.extractVariables("Hello {{name}}, your {{item}} is ready");
        assertThat(vars).contains("name", "item");
        assertThat(vars).hasSize(2);
    }

    @Test
    void shouldExtractOptionalVariables() {
        var vars = engine.extractVariables("Hello {{name}}{{?greeting}}");
        assertThat(vars).contains("name", "?greeting");
    }

    @Test
    void shouldReturnEmptySetForNullTemplate() {
        assertThat(engine.extractVariables(null)).isEmpty();
    }

    @Test
    void shouldRenderReservedVariables() {
        var version = createVersion("Tenant: {{tenantId}}, User: {{userId}}");
        when(versionRepository.findById(versionId)).thenReturn(Optional.of(version));

        var result = engine.preview(versionId, Map.of());

        assertThat(result.renderedPrompt()).contains("[tenantId]", "[userId]");
        assertThat(result.missingVariables()).isEmpty();
    }

    @Test
    void shouldWarnAboutUnsafePlaceholders() {
        var version = createVersion("Hello ${name}");
        when(versionRepository.findById(versionId)).thenReturn(Optional.of(version));

        var result = engine.preview(versionId, Map.of());

        assertThat(result.warnings()).isNotEmpty();
    }

    @Test
    void shouldRenderNestedVariables() {
        var version = createVersion("Hello {{user.name}}");
        when(versionRepository.findById(versionId)).thenReturn(Optional.of(version));

        var result = engine.preview(versionId, Map.of("user_name", "John"));

        assertThat(result.renderedPrompt()).isEqualTo("Hello John");
    }

    @Test
    void shouldValidateVariables() {
        var template = "Hello {{name}}, {{item}}";
        assertThrows(IllegalArgumentException.class, () ->
                engine.validateVariables(template, Map.of("name", "John")));
    }

    @Test
    void shouldPassValidationWithAllVariables() {
        var template = "Hello {{name}}, {{item}}";
        engine.validateVariables(template, Map.of("name", "John", "item", "Book"));
    }

    @Test
    void shouldRenderNullReturnNull() {
        assertThat(engine.render(null, Map.of())).isNull();
    }

    @Test
    void shouldEstimateTokensInPreview() {
        var version = createVersion("Hello {{name}}");
        when(versionRepository.findById(versionId)).thenReturn(Optional.of(version));

        var result = engine.preview(versionId, Map.of("name", "John"));

        assertThat(result.tokenEstimate()).isNotNull();
        assertThat(result.tokenEstimate().estimatedTotalTokens()).isGreaterThan(0);
    }

    private PromptVersionEntity createVersion(String promptBody) {
        var entity = new PromptVersionEntity();
        entity.setId(versionId);
        entity.setTemplateId(UUID.randomUUID());
        entity.setVersion(1);
        entity.setPromptBody(promptBody);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setCreatedBy(UUID.randomUUID());
        return entity;
    }
}
