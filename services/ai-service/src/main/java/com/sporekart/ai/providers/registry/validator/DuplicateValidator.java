package com.sporekart.ai.providers.registry.validator;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

public interface DuplicateValidator extends RegistryValidator {
    boolean isDuplicate(ProviderCatalogEntry entry);
    boolean isDuplicateId(String providerId);
    boolean isDuplicateName(String providerName);
}
