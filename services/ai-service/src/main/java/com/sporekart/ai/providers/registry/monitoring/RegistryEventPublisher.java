package com.sporekart.ai.providers.registry.monitoring;

import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

public interface RegistryEventPublisher {
    void publishProviderRegistered(ProviderCatalogEntry entry);
    void publishProviderActivated(String providerId);
    void publishProviderDeactivated(String providerId);
    void publishProviderDiscovered(ProviderCatalogEntry entry);
    void publishProviderHealthChanged(String providerId, boolean healthy);
    void publishProviderStatusChanged(String providerId, String oldStatus, String newStatus);
}
