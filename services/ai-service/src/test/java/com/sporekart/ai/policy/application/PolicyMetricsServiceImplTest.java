package com.sporekart.ai.policy.application;
import com.sporekart.ai.policy.api.PolicyMetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PolicyMetricsServiceImplTest {
    private PolicyMetricsServiceImpl metricsService;

    @BeforeEach void setUp() { metricsService = new PolicyMetricsServiceImpl(); }

    @Test void testRecordEvaluation() {
        metricsService.recordEvaluation(100, "ALLOW");
        assertEquals(1, metricsService.getEvaluationCount());
    }
    @Test void testRecordViolation() {
        metricsService.recordViolation("WARNING");
        assertEquals(1, metricsService.getViolationCount());
    }
    @Test void testCacheHits() { metricsService.recordCacheHit(); metricsService.recordCacheHit(); }
    @Test void testGetMetrics() {
        metricsService.recordEvaluation(50, "DENY");
        Map<String, Object> m = metricsService.getMetrics();
        assertTrue(m.containsKey("evaluationCount"));
    }
    @Test void testAverageTime() {
        metricsService.recordEvaluation(100, "ALLOW");
        metricsService.recordEvaluation(200, "ALLOW");
        assertEquals(150.0, metricsService.getAverageEvaluationTime(), 0.01);
    }
}
