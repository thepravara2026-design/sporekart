package com.sporekart.ai.providers.registry.validator;

import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;

public interface CapabilityConflictValidator extends RegistryValidator {
    boolean hasCapabilityConflicts(ProviderCatalogEntry entry);
    boolean hasVersionConflicts(ProviderCatalogEntry entry);
    List<String> findConflicts(ProviderCatalogEntry entry);
}
