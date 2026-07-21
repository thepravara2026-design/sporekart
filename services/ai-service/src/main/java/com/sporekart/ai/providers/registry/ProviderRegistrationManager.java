package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.util.List;
import java.util.Optional;

public interface ProviderRegistrationManager {
    ProviderCatalogEntry initiateRegistration(ProviderCatalogEntry entry);
    ProviderCatalogEntry validateRegistration(ProviderCatalogEntry entry);
    ProviderCatalogEntry approveRegistration(ProviderCatalogEntry entry);
    ProviderCatalogEntry completeRegistration(ProviderCatalogEntry entry);
    void cancelRegistration(String providerId);
    Optional<ProviderCatalogEntry> getRegistrationStatus(String providerId);
    List<ProviderCatalogEntry> getPendingRegistrations();
}
