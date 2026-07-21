package com.sporekart.ai.providers.health.checker;

import com.sporekart.ai.providers.health.HealthResponse;

import java.util.List;

public interface CompositeHealthChecker extends HealthChecker {
    void addChecker(HealthChecker checker);
    void removeChecker(String checkerName);
    List<HealthChecker> getCheckers();
    HealthResponse aggregate(List<HealthResponse> responses);
}
