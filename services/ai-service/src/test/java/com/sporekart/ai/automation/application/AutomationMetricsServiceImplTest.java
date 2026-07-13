package com.sporekart.ai.automation.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutomationMetricsServiceImplTest {

    private AutomationMetricsServiceImpl metricsService;

    @BeforeEach
    void setUp() {
        metricsService = new AutomationMetricsServiceImpl();
    }

    @Test
    void recordWorkflowShouldIncrementCounter() {
        assertEquals(0, metricsService.getTotalWorkflows());
        metricsService.recordWorkflow();
        assertEquals(1, metricsService.getTotalWorkflows());
        metricsService.recordWorkflow();
        assertEquals(2, metricsService.getTotalWorkflows());
    }

    @Test
    void recordJobShouldTrackSuccessAndFailure() {
        assertEquals(0, metricsService.getTotalJobs());
        assertEquals(1.0, metricsService.getJobSuccessRate(), 0.001);

        metricsService.recordJob(true);
        assertEquals(1, metricsService.getTotalJobs());
        assertEquals(1.0, metricsService.getJobSuccessRate(), 0.001);

        metricsService.recordJob(false);
        assertEquals(2, metricsService.getTotalJobs());
        assertEquals(0.5, metricsService.getJobSuccessRate(), 0.001);
    }

    @Test
    void recordRetryShouldIncrementCounter() {
        assertEquals(0, metricsService.getRetryCount());
        metricsService.recordRetry();
        assertEquals(1, metricsService.getRetryCount());
    }

    @Test
    void recordEscalationShouldIncrementCounter() {
        assertEquals(0, metricsService.getEscalationCount());
        metricsService.recordEscalation();
        assertEquals(1, metricsService.getEscalationCount());
    }

    @Test
    void recordLatencyShouldTrackAverage() {
        metricsService.recordLatency(100);
        metricsService.recordLatency(200);
        var stats = metricsService.getStatistics();
        assertEquals(300L, stats.get("totalLatencyMs"));
        assertEquals(2L, stats.get("latencySamples"));
        assertEquals(150.0, (double) stats.get("averageLatencyMs"), 0.001);
    }

    @Test
    void getStatisticsShouldReturnAllMetrics() {
        metricsService.recordWorkflow();
        metricsService.recordJob(true);
        metricsService.recordJob(false);
        metricsService.recordRetry();
        metricsService.recordEscalation();

        var stats = metricsService.getStatistics();
        assertEquals(1L, stats.get("totalWorkflows"));
        assertEquals(2L, stats.get("totalJobs"));
        assertEquals(1L, stats.get("successfulJobs"));
        assertEquals(1L, stats.get("failedJobs"));
        assertEquals(0.5, (double) stats.get("jobSuccessRate"), 0.001);
        assertEquals(1L, stats.get("retryCount"));
        assertEquals(1L, stats.get("escalationCount"));
        assertEquals(0L, stats.get("totalLatencyMs"));
        assertEquals(0L, stats.get("latencySamples"));
        assertEquals(0.0, (double) stats.get("averageLatencyMs"), 0.001);
    }
}
