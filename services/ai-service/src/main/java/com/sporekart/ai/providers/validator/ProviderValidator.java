package com.sporekart.ai.providers.validator;

import com.sporekart.ai.providers.ProviderConfiguration;
import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.metadata.ProviderMetadata;

import java.util.List;
import java.util.Optional;

public interface ProviderValidator {
    Optional<String> validate(ProviderConfiguration configuration);
    Optional<String> validate(ProviderMetadata metadata);
    Optional<String> validateCapability(String providerId, ProviderCapability capability);
    List<String> validateAll(ProviderConfiguration configuration);
    boolean isValid(ProviderConfiguration configuration);
    boolean supportsValidation(String providerId);
}
