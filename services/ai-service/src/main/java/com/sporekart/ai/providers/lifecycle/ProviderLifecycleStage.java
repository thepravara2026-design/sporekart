package com.sporekart.ai.providers.lifecycle;

public enum ProviderLifecycleStage {
    REGISTERED,
    VALIDATING,
    VALIDATED,
    INITIALIZING,
    INITIALIZED,
    ACTIVATING,
    ACTIVE,
    DEGRADED,
    MAINTENANCE,
    DEACTIVATING,
    DEACTIVATED,
    REMOVING,
    REMOVED,
    FAILED
}
