package com.sporekart.ai.providers.health.checker;

import com.sporekart.ai.providers.health.HealthResponse;

public interface HealthChecker {
    HealthResponse check(String providerId);
    boolean supports(String providerId);
    String checkerName();
}
