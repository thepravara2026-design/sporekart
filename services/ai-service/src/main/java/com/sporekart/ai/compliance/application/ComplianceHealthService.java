package com.sporekart.ai.compliance.application;

import java.util.Map;

public interface ComplianceHealthService {
    boolean isHealthy();
    Map<String, Object> getHealthDetails();
    Map<String, Object> getReadiness();
}
