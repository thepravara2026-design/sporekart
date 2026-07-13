package com.sporekart.ai.compliance.api;

import java.util.Map;

public interface ComplianceHealthService {
    boolean isHealthy();
    Map<String, Object> getHealthDetails();
    Map<String, Object> getReadiness();
}
