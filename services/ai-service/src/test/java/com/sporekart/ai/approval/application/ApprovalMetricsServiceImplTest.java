package com.sporekart.ai.approval.application;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApprovalMetricsServiceImplTest {

    private ApprovalMetricsServiceImpl metricsService;

    @BeforeEach
    void setUp() {
        metricsService = new ApprovalMetricsServiceImpl();
    }

    @Test
    void shouldRecordSubmission() {
        metricsService.recordSubmission();
        assertEquals(1, metricsService.getTotalRequests());
    }

    @Test
    void shouldRecordApproval() {
        metricsService.recordSubmission();
        metricsService.recordApproval(100);
        assertEquals(1, metricsService.getApprovedCount());
        assertEquals(100.0, metricsService.getAverageReviewTimeMs(), 0.01);
    }

    @Test
    void shouldRecordRejection() {
        metricsService.recordSubmission();
        metricsService.recordRejection(50);
        assertEquals(1, metricsService.getRejectedCount());
        assertEquals(50.0, metricsService.getAverageReviewTimeMs(), 0.01);
    }

    @Test
    void shouldReturnZeroAverageWhenNoReviews() {
        assertEquals(0.0, metricsService.getAverageReviewTimeMs(), 0.01);
    }

    @Test
    void shouldTrackMultipleMetrics() {
        metricsService.recordSubmission();
        metricsService.recordSubmission();
        metricsService.recordApproval(100);
        assertEquals(2, metricsService.getTotalRequests());
        assertEquals(1, metricsService.getApprovedCount());
        assertEquals(0, metricsService.getRejectedCount());
        assertEquals(100.0, metricsService.getAverageReviewTimeMs(), 0.01);
    }
}
