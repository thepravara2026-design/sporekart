package com.sporekart.ai.providers.failover;

import java.util.Optional;

public interface FailoverManager {
    Optional<String> getFailoverTarget(String providerId);
    boolean executeFailover(String providerId, String targetProviderId);
    boolean isFailoverActive(String providerId);
    void completeFailover(String providerId);
    void cancelFailover(String providerId);
}
