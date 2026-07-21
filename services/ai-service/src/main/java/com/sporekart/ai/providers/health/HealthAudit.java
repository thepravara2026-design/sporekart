package com.sporekart.ai.providers.health;

public interface HealthAudit {
    void recordHealthCheck(String providerId, HealthStatus status);
    void recordStatusChange(String providerId, HealthStatus from, HealthStatus to);
    void recordAlert(String providerId, String message);
    void clearAuditLog(String providerId);
}
