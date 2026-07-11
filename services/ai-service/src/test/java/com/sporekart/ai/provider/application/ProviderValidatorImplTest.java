package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.ProviderValidator;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.provider.infrastructure.GeminiAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderValidatorImplTest {

    private ProviderRegistryImpl registry;
    private ProviderValidator validator;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistryImpl();
        registry.register("GEMINI", new GeminiAdapter());
        validator = new ProviderValidatorImpl(registry);
    }

    @Test
    void shouldValidateExistingProvider() {
        assertThat(validator.validateProvider("GEMINI")).isTrue();
        assertThat(validator.getValidationError()).isNull();
    }

    @Test
    void shouldRejectNullProvider() {
        assertThat(validator.validateProvider(null)).isFalse();
        assertThat(validator.getValidationError()).isNotNull();
    }

    @Test
    void shouldRejectUnknownProvider() {
        assertThat(validator.validateProvider("UNKNOWN")).isFalse();
        assertThat(validator.getValidationError()).contains("not registered");
    }

    @Test
    void shouldValidateExistingModel() {
        assertThat(validator.validateModel("GEMINI", "gemini-pro")).isTrue();
    }

    @Test
    void shouldRejectBlankModel() {
        assertThat(validator.validateModel("GEMINI", "")).isFalse();
    }

    @Test
    void shouldValidateValidRequest() {
        AiRequest request = new AiRequest("Hello", Map.of());
        assertThat(validator.validateRequest(request, "GEMINI")).isTrue();
    }

    @Test
    void shouldRejectNullRequest() {
        assertThat(validator.validateRequest(null, "GEMINI")).isFalse();
    }

    @Test
    void shouldRejectBlankPrompt() {
        AiRequest request = new AiRequest("");
        assertThat(validator.validateRequest(request, "GEMINI")).isFalse();
    }
}
