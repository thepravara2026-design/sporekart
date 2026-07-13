package com.sporekart.ai.policy.api;

import java.util.Map;

public interface PolicyMetricsService {
    void recordEvaluation(long timeMs, String decision);
    void recordViolation(String severity);
    void recordCacheHit();
    void recordCacheMiss();
    void recordPolicyActivation();
    void recordPolicyDeactivation();
    Map<String, Object> getMetrics();
    long getEvaluationCount();
    long getViolationCount();
    double getAverageEvaluationTime();
}
