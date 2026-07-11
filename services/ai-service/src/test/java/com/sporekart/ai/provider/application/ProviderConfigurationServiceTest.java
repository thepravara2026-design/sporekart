package com.sporekart.ai.provider.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderConfigurationServiceTest {

    private ProviderConfigurationService configService;

    @BeforeEach
    void setUp() {
        configService = new ProviderConfigurationService(null);
    }

    @Test
    void shouldReturnDefaultConfig() {
        var endpoint = configService.getProperty("GEMINI", "endpoint");
        assertThat(endpoint).isPresent();
        assertThat(endpoint.get()).contains("googleapis");
    }

    @Test
    void shouldReturnEmptyForUnknownProvider() {
        var prop = configService.getProperty("UNKNOWN", "key");
        assertThat(prop).isEmpty();
    }

    @Test
    void shouldReturnAllProperties() {
        var props = configService.getAllProperties("OPENAI");
        assertThat(props).containsKey("endpoint");
        assertThat(props).containsKey("model");
    }

    @Test
    void shouldSetProperty() {
        configService.setProperty("GEMINI", "customKey", "customValue");
        var value = configService.getProperty("GEMINI", "customKey");
        assertThat(value).isPresent().contains("customValue");
    }

    @Test
    void shouldUpdateConfig() {
        configService.updateProviderConfig("GEMINI", java.util.Map.of("key1", "value1"));
        var value = configService.getProperty("GEMINI", "key1");
        assertThat(value).isPresent().contains("value1");
    }

    @Test
    void shouldReloadDefaults() {
        configService.setProperty("GEMINI", "customProp", "custom");
        configService.reload();
        var custom = configService.getProperty("GEMINI", "customProp");
        assertThat(custom).isEmpty();
    }
}
