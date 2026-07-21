package com.sporekart.ai.providers.registry.synchronization;

import java.time.Instant;

public record SyncEvent(
    String eventId,
    String providerId,
    SyncEventType eventType,
    Instant timestamp,
    boolean success,
    String details
) {
    public enum SyncEventType {
        METADATA_SYNC,
        CAPABILITY_SYNC,
        HEALTH_SYNC,
        CONFIGURATION_SYNC,
        DISCOVERY_SYNC,
        REGISTRY_REPLICATION,
        FULL_SYNC
    }
}
