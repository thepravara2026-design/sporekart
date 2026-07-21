package com.sporekart.ai.providers;

import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.contracts.ChatContract;
import com.sporekart.ai.providers.exception.*;
import com.sporekart.ai.providers.lifecycle.ProviderLifecycleStage;
import com.sporekart.ai.providers.metadata.ProviderMetadata;
import com.sporekart.ai.providers.model.ProviderOperation;
import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.model.ProviderResponse;
import com.sporekart.ai.providers.selector.strategies.PrioritySelectionStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProviderDomainTest {

    @Test
    void shouldHaveAllCapabilityValues() {
        assertEquals(20, ProviderCapability.values().length);
    }

    @Test
    void shouldHaveAllProviderTypes() {
        assertEquals(10, ProviderType.values().length);
    }

    @Test
    void shouldHaveAllStatuses() {
        assertEquals(9, ProviderStatus.values().length);
    }

    @Test
    void shouldHaveAllLifecycleStages() {
        assertEquals(14, ProviderLifecycleStage.values().length);
    }

    @Test
    void shouldHaveAllOperations() {
        assertEquals(16, ProviderOperation.values().length);
    }

    @Test
    void shouldCreateProviderRequest() {
        var req = new ProviderRequest("req-1", "openai", "gpt-4", "chat", "Hello",
            null, null, 0.7, 100, false, "user-1", "tenant-1", null, null);
        assertEquals("openai", req.providerId());
        assertEquals("Hello", req.getPrompt().orElseThrow());
    }

    @Test
    void shouldCreateProviderResponse() {
        var ok = ProviderResponse.ok("req-1", "openai", "gpt-4", List.of("Hello!"));
        assertTrue(ok.success());
        assertEquals(200, ok.statusCode());

        var err = ProviderResponse.error("req-1", "openai", 400, "BAD_REQUEST", "Invalid");
        assertFalse(err.success());
        assertTrue(err.getErrorCode().isPresent());
    }

    @Test
    void shouldCreateProviderHealth() {
        var healthy = ProviderHealth.healthy("openai");
        assertTrue(healthy.available());
        assertEquals("HEALTHY", healthy.status());

        var unhealthy = ProviderHealth.unhealthy("openai", "Timeout");
        assertFalse(unhealthy.available());
        assertEquals("UNHEALTHY", unhealthy.status());
    }

    @Test
    void shouldCreateProviderMetadata() {
        var meta = new ProviderMetadata("openai", "OpenAI", ProviderType.OPENAI, "v1",
            "GPT models", "OpenAI", "https://openai.com", "https://docs.openai.com",
            List.of("gpt-4", "gpt-3.5"), List.of("us-east"), Map.of("standard", "$0.01"),
            128000, 10000, 1000000, true, null, null, ProviderStatus.ACTIVE);
        assertEquals("openai", meta.providerId());
        assertEquals(ProviderType.OPENAI, meta.type());
    }

    @Test
    void shouldCreateChatContract() {
        var msg = new ChatContract.Message("user", "Hello");
        var contract = new ChatContract("gpt-4", List.of(msg), 0.7, 100, false);
        assertEquals("gpt-4", contract.model());
        assertEquals(1, contract.messages().size());
    }

    @Test
    void shouldThrowProviderUnavailableException() {
        var ex = assertThrows(ProviderUnavailableException.class,
            () -> { throw new ProviderUnavailableException("openai"); });
        assertEquals("PROVIDER_UNAVAILABLE", ex.getErrorCode());
        assertEquals(503, ex.getStatusCode());
    }

    @Test
    void shouldThrowUnsupportedCapabilityException() {
        var ex = assertThrows(UnsupportedCapabilityException.class,
            () -> { throw new UnsupportedCapabilityException("openai", "vision"); });
        assertEquals("UNSUPPORTED_CAPABILITY", ex.getErrorCode());
    }

    @Test
    void shouldThrowProviderConfigurationException() {
        var ex = assertThrows(ProviderConfigurationException.class,
            () -> { throw new ProviderConfigurationException("openai", "Missing API key"); });
        assertEquals("PROVIDER_CONFIG_ERROR", ex.getErrorCode());
    }

    @Test
    void shouldThrowProviderAuthenticationException() {
        var ex = assertThrows(ProviderAuthenticationException.class,
            () -> { throw new ProviderAuthenticationException("openai"); });
        assertEquals("PROVIDER_AUTH_ERROR", ex.getErrorCode());
    }

    @Test
    void shouldThrowProviderRateLimitException() {
        var ex = assertThrows(ProviderRateLimitException.class,
            () -> { throw new ProviderRateLimitException("openai", 30); });
        assertEquals("PROVIDER_RATE_LIMITED", ex.getErrorCode());
        assertEquals(30, ex.getRetryAfterSeconds());
    }

    @Test
    void shouldThrowProviderTimeoutException() {
        var ex = assertThrows(ProviderTimeoutException.class,
            () -> { throw new ProviderTimeoutException("openai", 30000); });
        assertEquals("PROVIDER_TIMEOUT", ex.getErrorCode());
        assertEquals(504, ex.getStatusCode());
    }

    @Test
    void shouldThrowProviderHealthException() {
        var ex = assertThrows(ProviderHealthException.class,
            () -> { throw new ProviderHealthException("openai", "Down"); });
        assertEquals("PROVIDER_HEALTH_ERROR", ex.getErrorCode());
    }

    @Test
    void shouldThrowProviderDiscoveryException() {
        var ex = assertThrows(ProviderDiscoveryException.class,
            () -> { throw new ProviderDiscoveryException("No providers found"); });
        assertEquals("PROVIDER_DISCOVERY_ERROR", ex.getErrorCode());
    }

    @Test
    void shouldThrowProviderValidationException() {
        var ex = assertThrows(ProviderValidationException.class,
            () -> { throw new ProviderValidationException("openai", List.of("Missing field")); });
        assertEquals("PROVIDER_VALIDATION_ERROR", ex.getErrorCode());
        assertEquals(1, ex.getValidationErrors().size());
    }

    @Test
    void shouldThrowProviderSelectionException() {
        var ex = assertThrows(ProviderSelectionException.class,
            () -> { throw new ProviderSelectionException("No provider matches"); });
        assertEquals("PROVIDER_SELECTION_ERROR", ex.getErrorCode());
    }

    @Test
    void shouldSupportCapabilityWithProfile() {
        var profile = new com.sporekart.ai.providers.capability.CapabilityProfile("openai",
            List.of(ProviderCapability.CHAT, ProviderCapability.STREAMING), Map.of(), Map.of());
        assertTrue(profile.hasCapability(ProviderCapability.CHAT));
        assertFalse(profile.hasCapability(ProviderCapability.VISION));
    }

    @Test
    void shouldApplyPriorityStrategy() {
        var strategy = new PrioritySelectionStrategy();
        assertEquals("priority", strategy.name());
        assertEquals(1, strategy.order());
    }

    @Test
    void shouldCreateProviderCredentials() {
        var creds = new com.sporekart.ai.providers.model.ProviderCredentials(
            "sk-xxx", null, null, null, null, null, null);
        assertTrue(creds.hasCredentials());
    }

    @Test
    void shouldCreateValidationResult() {
        var valid = com.sporekart.ai.providers.validator.ValidationResult.valid();
        assertTrue(valid.isValid());

        var invalid = com.sporekart.ai.providers.validator.ValidationResult.invalid(List.of("Missing field"));
        assertFalse(invalid.isValid());
        assertEquals(1, invalid.errors().size());
    }
}
