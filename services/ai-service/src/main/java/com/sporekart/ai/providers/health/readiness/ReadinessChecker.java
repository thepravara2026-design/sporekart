package com.sporekart.ai.providers.health.readiness;

public interface ReadinessChecker {
    ReadinessState checkReadiness(String providerId);
    boolean isReady(String providerId);
    boolean isNotReady(String providerId);
    String getReadinessReason(String providerId);
}
