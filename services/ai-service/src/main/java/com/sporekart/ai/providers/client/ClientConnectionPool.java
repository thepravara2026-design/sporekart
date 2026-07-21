package com.sporekart.ai.providers.client;

import java.time.Duration;

public interface ClientConnectionPool {
    boolean acquire(String providerId);
    void release(String providerId);
    int activeConnections(String providerId);
    int availableConnections(String providerId);
    int maxConnections(String providerId);
    Duration averageWaitTime(String providerId);
}
