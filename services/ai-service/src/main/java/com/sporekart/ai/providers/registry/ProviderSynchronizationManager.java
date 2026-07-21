package com.sporekart.ai.providers.registry;

import java.time.Instant;

public interface ProviderSynchronizationManager {
    void syncAll();
    void syncProvider(String providerId);
    void syncMetadata(String providerId);
    void syncCapabilities(String providerId);
    void syncHealth(String providerId);
    void syncConfiguration(String providerId);
    boolean isSynchronized(String providerId);
    Instant getLastSyncTime(String providerId);
}
