package com.sporekart.ai.configuration.api;

import com.sporekart.ai.configuration.model.secret.SecretProviderType;
import com.sporekart.ai.configuration.model.secret.SecretReference;
import com.sporekart.ai.configuration.model.secret.SecretScope;

import java.util.Optional;
import java.util.Set;

public interface SecretProvider {
    SecretProviderType type();
    boolean supports(SecretReference reference);
    Optional<String> resolve(SecretReference reference);
    String resolveOrDefault(SecretReference reference, String defaultValue);
    Set<String> listKeys(SecretScope scope);
    boolean isAvailable();
    boolean isSecret(String key);
}
