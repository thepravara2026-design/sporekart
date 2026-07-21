package com.sporekart.ai.providers.health.liveness;

public interface LivenessChecker {
    LivenessState checkLiveness(String providerId);
    boolean isAlive(String providerId);
    boolean isDead(String providerId);
    String getLivenessReason(String providerId);
}
