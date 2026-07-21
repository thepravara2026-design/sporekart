package com.sporekart.ai.providers.registry.synchronization;

public interface SyncStrategy {
    void sync();
    boolean isSupported();
    String strategyName();
}
