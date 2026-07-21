package com.sporekart.ai.providers.health;

public interface HealthController {
    HealthStatus getOverallHealth();
    boolean isSystemHealthy();
    boolean isSystemDegraded();
    boolean isSystemCritical();
    int getHealthyProviderCount();
    int getUnhealthyProviderCount();
}
