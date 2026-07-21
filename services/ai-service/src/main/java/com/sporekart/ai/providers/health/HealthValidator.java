package com.sporekart.ai.providers.health;

public interface HealthValidator {
    boolean isValid(HealthResponse response);
    boolean isValidStatus(HealthStatus status);
    boolean isThresholdValid(double threshold);
}
