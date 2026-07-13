package com.sporekart.ai.decision.api;

import com.sporekart.ai.decision.domain.*;
import java.util.Map;

public interface DecisionMetricsService {
    void recordDecision(String action, DecisionConfidence confidence, long timeMs);
    void recordConflict(String strategy);
    void recordReplay();
    DecisionStatistics getStatistics();
    Map<String, Object> getDetailedMetrics();
    long getTotalDecisions();
    double getAverageConfidence();
}
