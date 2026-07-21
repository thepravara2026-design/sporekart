package com.sporekart.ai.providers.registry.validator;

import com.sporekart.ai.providers.metadata.ProviderMetadata;

public interface MetadataValidator extends RegistryValidator {
    boolean hasRequiredFields(ProviderMetadata metadata);
    boolean hasValidVersion(ProviderMetadata metadata);
    boolean hasValidType(ProviderMetadata metadata);
}
