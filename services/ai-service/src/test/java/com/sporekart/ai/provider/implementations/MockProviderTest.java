package com.sporekart.ai.provider.implementations;

import com.sporekart.ai.provider.models.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class MockProviderTest {

    @Test
    void shouldGenerateCompletion() {
        var provider = new MockAIProvider("test-1", "TEST");
        var request = ProviderRequest.of("Hello", "model-1");
        var response = provider.generateCompletion(request);
        assertTrue(response.success());
        assertNotNull(response.content());
    }

    @Test
    void shouldGenerateChat() {
        var provider = new MockAIProvider("test-1", "TEST");
        var response = provider.generateChat(ProviderRequest.of("Hi", "model-1"));
        assertTrue(response.success());
    }

    @Test
    void shouldGenerateEmbeddings() {
        var provider = new MockAIProvider("test-1", "TEST");
        var response = provider.generateEmbeddings(ProviderRequest.of("text", "model-1"));
        assertTrue(response.success());
    }

    @Test
    void shouldGenerateStreaming() {
        var provider = new MockAIProvider("test-1", "TEST");
        var response = provider.generateStreaming(ProviderRequest.of("text", "model-1"));
        assertTrue(response.success());
    }

    @Test
    void shouldGenerateAsync() throws Exception {
        var provider = new MockAIProvider("test-1", "TEST");
        CompletableFuture<ProviderResponse> future = provider.generateChatAsync(ProviderRequest.of("Hello", "model-1"));
        var response = future.get();
        assertTrue(response.success());
    }

    @Test
    void shouldCheckHealth() {
        var provider = new MockAIProvider("test-1", "TEST");
        var health = provider.checkHealth();
        assertEquals(ProviderHealth.HealthStatus.HEALTHY, health.status());
    }

    @Test
    void shouldBeAvailable() {
        var provider = new MockAIProvider("test-1", "TEST");
        assertTrue(provider.isAvailable());
    }

    @Test
    void shouldReturnCapabilities() {
        var provider = new MockAIProvider("test-1", "TEST");
        var caps = provider.getCapabilities();
        assertNotNull(caps);
        assertEquals("TEST", caps.providerName());
    }

    @Test
    void shouldReturnSupportedModels() {
        var provider = new MockAIProvider("test-1", "TEST");
        assertFalse(provider.supportedModels().isEmpty());
    }

    @Test
    void shouldReturnVersion() {
        var provider = new MockAIProvider("test-1", "TEST", "2.0.0");
        assertTrue(provider.version().isPresent());
        assertEquals("2.0.0", provider.version().get());
    }

    @Test
    void shouldReturnCostInfo() {
        var provider = new MockAIProvider("test-1", "OPENAI");
        var cost = provider.costInfo();
        assertNotNull(cost);
        assertTrue(cost.costPerInputToken() > 0);
    }

    @Test
    void shouldReturnRateLimits() {
        var provider = new MockAIProvider("test-1", "OPENAI");
        var limits = provider.rateLimits();
        assertNotNull(limits);
    }

    @Test
    void shouldReturnMaxTokens() {
        var provider = new MockAIProvider("test-1", "CLAUDE");
        assertEquals(100000, provider.maxTokens());
    }

    @Test
    void shouldReflectAvailabilityChanges() {
        var provider = new MockAIProvider("test-1", "TEST");
        provider.setAvailable(false);
        assertFalse(provider.isAvailable());
        assertEquals(ProviderHealth.HealthStatus.UNHEALTHY, provider.checkHealth().status());
    }

    @Test
    void shouldHaveProviderId() {
        var provider = new MockAIProvider("custom-id", "CUSTOM");
        assertEquals("custom-id", provider.providerId());
    }

    @Test
    void shouldHaveProviderName() {
        var provider = new MockAIProvider("test-1", "OPENAI");
        assertEquals("OPENAI", provider.providerName());
    }

    @Test
    void openaiProviderHasCorrectModels() {
        var provider = new MockAIProvider("openai-1", "OPENAI");
        assertTrue(provider.supportedModels().contains("gpt-4"));
        assertTrue(provider.supportedModels().contains("gpt-4-turbo"));
    }

    @Test
    void geminiProviderHasVision() {
        var provider = new MockAIProvider("gemini-1", "GEMINI");
        assertTrue(provider.getCapabilities().supportsVision());
        assertTrue(provider.supportedModalities().contains("vision"));
    }

    @Test
    void claudeProviderHasReasoning() {
        var provider = new MockAIProvider("claude-1", "CLAUDE");
        assertTrue(provider.getCapabilities().supportsReasoning());
    }

    @Test
    void ollamaProviderIsFree() {
        var provider = new MockAIProvider("ollama-1", "OLLAMA");
        assertEquals(ProviderCostInfo.PricingTier.FREE, provider.costInfo().pricingTier());
        assertTrue(provider.rateLimits().unlimited());
    }

    @Test
    void groqProviderHasLowCost() {
        var provider = new MockAIProvider("groq-1", "GROQ");
        assertEquals(ProviderCostInfo.PricingTier.LOW, provider.costInfo().pricingTier());
    }

    @Test
    void shouldDefineAllStandardProviders() {
        assertEquals(10, ProviderDefinitions.ALL_PROVIDERS.size());
    }

    @Test
    void shouldRegisterAllMockProviders() {
        var reg = new com.sporekart.ai.provider.registry.ProviderRegistryImpl();
        ProviderDefinitions.registerAllMockProviders(reg);
        assertEquals(10, reg.providerCount());

        assertTrue(reg.isRegistered("openai-1"));
        assertTrue(reg.isRegistered("gemini-1"));
        assertTrue(reg.isRegistered("claude-1"));
        assertTrue(reg.isRegistered("azure-openai-1"));
        assertTrue(reg.isRegistered("ollama-1"));
        assertTrue(reg.isRegistered("groq-1"));
        assertTrue(reg.isRegistered("mistral-1"));
        assertTrue(reg.isRegistered("openrouter-1"));
        assertTrue(reg.isRegistered("bedrock-1"));
        assertTrue(reg.isRegistered("togetherai-1"));
    }
}
