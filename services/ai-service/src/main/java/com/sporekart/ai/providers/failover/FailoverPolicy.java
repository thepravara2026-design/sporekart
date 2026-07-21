package com.sporekart.ai.providers.failover;

public interface FailoverPolicy {
    boolean isFailoverAllowed(String providerId);
    String selectTarget(String providerId, String preferredRegion);
    boolean isHealthCheckRequired();
    boolean isAutomaticFailoverEnabled();
    int getMaxFailoverAttempts();
}
