package com.sporekart.ai.decision.api;

import java.util.Map;

public interface DecisionHealthService {
    Map<String, Object> checkHealth();
    Map<String, Object> getStatus();
    boolean isOperational();
    Map<String, Object> getMetrics();
}
