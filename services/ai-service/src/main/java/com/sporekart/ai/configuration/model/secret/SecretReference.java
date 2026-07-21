package com.sporekart.ai.configuration.model.secret;

public record SecretReference(
    String secretKey,
    SecretProviderType providerType,
    SecretScope scope,
    String description,
    boolean required,
    String defaultValue,
    String fallbackEnvVar,
    boolean cacheable
) {
    public static SecretReference of(String secretKey, SecretProviderType providerType) {
        return new SecretReference(secretKey, providerType, SecretScope.PLATFORM, "", true, null, null, true);
    }

    public SecretReference withDescription(String description) {
        return new SecretReference(secretKey, providerType, scope, description, required, defaultValue, fallbackEnvVar, cacheable);
    }

    public SecretReference withFallbackEnvironmentVariable(String envVar) {
        return new SecretReference(secretKey, providerType, scope, description, required, defaultValue, envVar, cacheable);
    }
}
