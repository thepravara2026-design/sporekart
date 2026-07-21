package com.sporekart.ai.providers.registry.monitoring;

public interface RegistryMetricsCollector {
    void incrementProviderCount();
    void decrementProviderCount();
    void recordDiscoveryEvent();
    void recordRegistrationEvent();
    void recordActivationEvent();
    void recordHealthCheck();
    void recordCacheHit();
    void recordCacheMiss();
    int getProviderCount();
    int getCapabilityCount();
    int getDiscoveryCount();
    int getRegistrationCount();
}
