package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.DecisionHealthService;
import com.sporekart.ai.decision.api.DecisionMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DecisionHealthServiceImpl implements DecisionHealthService {
    private final DecisionMetricsService metricsService;

    @Override public Map<String, Object> checkHealth() {
        return Map.of("status", "UP", "service", "decision-engine",
            "timestamp", System.currentTimeMillis(), "details", metricsService.getDetailedMetrics());
    }
    @Override public Map<String, Object> getStatus() {
        return Map.of("operational", true, "mode", "DEVELOPMENT", "totalDecisions", metricsService.getTotalDecisions());
    }
    @Override public boolean isOperational() { return true; }
    @Override public Map<String, Object> getMetrics() { return metricsService.getDetailedMetrics(); }
}
