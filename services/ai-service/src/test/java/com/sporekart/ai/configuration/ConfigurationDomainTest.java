package com.sporekart.ai.configuration;

import com.sporekart.ai.configuration.domain.*;
import com.sporekart.ai.configuration.model.environment.Environment;
import com.sporekart.ai.configuration.model.environment.EnvironmentVariable;
import com.sporekart.ai.configuration.model.feature.FeatureFlag;
import com.sporekart.ai.configuration.model.feature.FeatureFlagRegistry;
import com.sporekart.ai.configuration.model.feature.FeatureScope;
import com.sporekart.ai.configuration.model.secret.SecretProviderType;
import com.sporekart.ai.configuration.model.secret.SecretReference;
import com.sporekart.ai.configuration.model.secret.SecretScope;
import com.sporekart.ai.configuration.validation.ConfigValidationResult;
import com.sporekart.ai.configuration.validation.ConfigValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationDomainTest {

    @Test
    void shouldCreateAIConfigurationDefaults() {
        var config = AIConfiguration.defaults();
        assertTrue(config.enabled());
        assertEquals("openai", config.defaultProvider());
        assertEquals(3, config.maxRetries());
    }

    @Test
    void shouldCreateGatewayConfigurationDefaults() {
        var config = GatewayConfiguration.defaults();
        assertTrue(config.enabled());
        assertEquals("/api/v1/ai", config.basePath());
        assertTrue(config.rateLimiterEnabled());
    }

    @Test
    void shouldCreateRuntimeConfigurationDefaults() {
        var config = RuntimeConfiguration.defaults();
        assertTrue(config.enabled());
        assertEquals(1000, config.maxAgentExecutions());
        assertTrue(config.hotReloadEnabled());
    }

    @Test
    void shouldCreateConfigKeyWithValidation() {
        assertThrows(IllegalArgumentException.class, () -> new ConfigKey("", "test", "", String.class, false, false, false, ConfigurationSource.DEFAULT, ConfigurationStatus.ACTIVE, null));
        var key = ConfigKey.of("test.key", String.class).withDescription("Test key");
        assertEquals("test.key", key.key());
        assertEquals(String.class, key.type());
    }

    @Test
    void shouldCreateConfigValue() {
        var key = ConfigKey.of("test.key", String.class);
        var value = ConfigValue.of(key, "test-value");
        assertEquals("test-value", value.value());
        assertEquals(ConfigurationSource.DEFAULT, value.source());
    }

    @Test
    void shouldValidateConfiguration() {
        var key = ConfigKey.of("test.url", String.class).withDescription("Test URL");
        var validResult = ConfigValidator.validateUrl(key, "https://example.com");
        assertTrue(validResult.isValid());

        var invalidResult = ConfigValidator.validateUrl(key, "not-a-url");
        assertFalse(invalidResult.isValid());
    }

    @Test
    void shouldCreateFeatureFlagRegistry() {
        var registry = new FeatureFlagRegistry();
        registry.register(FeatureFlag.of("test-flag", "Test flag", true));
        assertTrue(registry.isEnabled("test-flag"));
        assertEquals(1, registry.size());
    }

    @Test
    void shouldDetectEnvironment() {
        assertEquals("local", Environment.LOCAL.profile());
        assertTrue(Environment.PROD.isProduction());
        assertTrue(Environment.PROD.requiresSecrets());
        assertFalse(Environment.LOCAL.isProduction());
    }

    @Test
    void shouldCreateEnvironmentVariable() {
        var envVar = EnvironmentVariable.of("AI_TEST_VAR", "Test variable").markRequired().markSecret();
        assertTrue(envVar.required());
        assertTrue(envVar.secret());
        assertEquals("AI_TEST_VAR", envVar.variableName());
    }

    @Test
    void shouldCreateSecretReference() {
        var ref = SecretReference.of("test.secret", SecretProviderType.ENVIRONMENT_VARIABLES)
            .withDescription("Test secret");
        assertEquals("test.secret", ref.secretKey());
        assertEquals(SecretProviderType.ENVIRONMENT_VARIABLES, ref.providerType());
    }

    @Test
    void shouldCreateAllProviderConfigurations() {
        var openai = com.sporekart.ai.configuration.model.provider.OpenAiConfig.defaults();
        assertTrue(openai.enabled());
        assertEquals(10, openai.models().size());

        var gemini = com.sporekart.ai.configuration.model.provider.GeminiConfig.defaults();
        assertTrue(gemini.enabled());
        assertEquals(5, gemini.models().size());

        var claude = com.sporekart.ai.configuration.model.provider.ClaudeConfig.defaults();
        assertTrue(claude.enabled());
        assertEquals(4, claude.models().size());
    }

    @Test
    void shouldCreateConfigValidationResult() {
        var success = ConfigValidationResult.success("test.key");
        assertTrue(success.isValid());

        var failed = ConfigValidationResult.failed("test.key",
            com.sporekart.ai.configuration.validation.ConfigValidationError.error("test.key", "error"));
        assertFalse(failed.isValid());
        assertTrue(failed.hasErrors());
    }

    @Test
    void shouldCreateTenantConfiguration() {
        var tenant = TenantConfiguration.defaults("tenant-1", "Test Tenant");
        assertEquals("tenant-1", tenant.tenantId());
        assertTrue(tenant.enabled());
    }

    @Test
    void shouldCreateConversationConfiguration() {
        var config = ConversationConfiguration.defaults();
        assertTrue(config.enabled());
        assertEquals(30, config.sessionTimeoutMinutes());
    }

    @Test
    void shouldCreateMemoryConfiguration() {
        var config = MemoryConfiguration.defaults();
        assertTrue(config.enabled());
        assertTrue(config.vectorIndexingEnabled());
        assertEquals(100, config.maxShortTermEntries());
    }

    @Test
    void shouldCreateAnalyticsConfiguration() {
        var config = AnalyticsConfiguration.defaults();
        assertTrue(config.enabled());
        assertEquals(90, config.dataRetentionDays());
    }

    @Test
    void shouldCreateSecurityConfiguration() {
        var config = SecurityConfiguration.defaults();
        assertTrue(config.enabled());
        assertEquals("environment", config.secretProviderType());
    }

    @Test
    void shouldCreateMonitoringConfiguration() {
        var config = MonitoringConfiguration.defaults();
        assertTrue(config.enabled());
        assertTrue(config.prometheusEnabled());
    }

    @Test
    void shouldCreateSemanticConfiguration() {
        var config = SemanticConfiguration.defaults();
        assertEquals("HYBRID", config.defaultSearchType());
        assertEquals(0.7, config.semanticWeight());
    }

    @Test
    void shouldCreateEmbeddingConfiguration() {
        var config = EmbeddingConfiguration.defaults();
        assertEquals("openai", config.defaultProvider());
        assertEquals(1536, config.embeddingDimension());
    }

    @Test
    void shouldCreatePromptConfiguration() {
        var config = PromptConfiguration.defaults();
        assertTrue(config.versioningEnabled());
        assertEquals(32000, config.maxPromptLength());
    }
}
