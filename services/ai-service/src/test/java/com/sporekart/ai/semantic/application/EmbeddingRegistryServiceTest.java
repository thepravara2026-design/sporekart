package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.EmbeddingProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingRegistryServiceTest {

    private EmbeddingRegistryService service;

    @BeforeEach
    void setUp() {
        service = new EmbeddingRegistryService();
        service.initializeDefaults();
    }

    @Test
    void testGetProvider() {
        Optional<EmbeddingRegistryService.ProviderConfig> config = service.getProvider("OPENAI");
        assertTrue(config.isPresent());
        assertEquals("OpenAI", config.get().name());
    }

    @Test
    void testGetByEnum() {
        Optional<EmbeddingRegistryService.ProviderConfig> config = service.getProvider(EmbeddingProvider.GEMINI);
        assertTrue(config.isPresent());
    }

    @Test
    void testGetProviderNotFound() {
        assertFalse(service.getProvider("UNKNOWN").isPresent());
    }

    @Test
    void testListProviders() {
        assertFalse(service.listProviders().isEmpty());
    }

    @Test
    void testListAvailableProviders() {
        assertFalse(service.listAvailableProviders().isEmpty());
    }

    @Test
    void testIsProviderAvailable() {
        assertTrue(service.isProviderAvailable("OPENAI"));
    }

    @Test
    void testIsProviderAvailableReturnsFalseForUnknown() {
        assertFalse(service.isProviderAvailable("UNKNOWN"));
    }

    @Test
    void testGetDefaultDimensions() {
        assertEquals(1536, service.getDefaultDimensions("OPENAI"));
    }

    @Test
    void testGetModels() {
        assertFalse(service.getModels("OPENAI").isEmpty());
    }

    @Test
    void testIsValidModel() {
        assertTrue(service.isValidModel("OPENAI", "text-embedding-3-small"));
        assertFalse(service.isValidModel("OPENAI", "invalid-model"));
    }

    @Test
    void testSetAvailability() {
        service.setAvailability("BEDROCK", false);
        assertFalse(service.isProviderAvailable("BEDROCK"));
    }

    @Test
    void testRegisterProvider() {
        var config = new EmbeddingRegistryService.ProviderConfig(
                EmbeddingProvider.CUSTOM, "CustomNew", java.util.List.of("custom"), 512, 10, true);
        service.registerProvider(config);
        assertTrue(service.isProviderAvailable("CUSTOM"));
    }
}
