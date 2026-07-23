package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.dto.PerformanceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceAnalyticsEngineTest {

    private PerformanceAnalyticsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new PerformanceAnalyticsEngine();
    }

    @Test
    void analyzePerformance_shouldReturnValidResponse() {
        var request = new PerformanceRequest(null, "current_quarter");
        var response = engine.analyzePerformance(request);
        assertNotNull(response);
        assertNotNull(response.departments());
        assertFalse(response.departments().isEmpty());
    }

    @Test
    void analyzePerformance_shouldHaveFiveDepartments() {
        var request = new PerformanceRequest(null, "current_quarter");
        var response = engine.analyzePerformance(request);
        assertEquals(5, response.departments().size());
    }

    @Test
    void analyzePerformance_shouldHaveCrossDepartmental() {
        var request = new PerformanceRequest(null, "current_quarter");
        var response = engine.analyzePerformance(request);
        assertNotNull(response.crossDepartmental());
        assertTrue(response.crossDepartmental().containsKey("overallAvgScore"));
    }

    @Test
    void analyzePerformance_withDepartment_shouldFilter() {
        var request = new PerformanceRequest("Training", "current_quarter");
        var response = engine.analyzePerformance(request);
        assertEquals(1, response.departments().size());
        assertEquals("Training", response.departments().get(0).name());
    }

    @Test
    void analyzePerformance_withUnknownDepartment_shouldReturnEmpty() {
        var request = new PerformanceRequest("Unknown", "current_quarter");
        var response = engine.analyzePerformance(request);
        assertTrue(response.departments().isEmpty());
    }

    @Test
    void getDepartmentDetail_shouldReturnValidDetail() {
        var detail = engine.getDepartmentDetail("Operations");
        assertNotNull(detail);
        assertEquals("Operations", detail.department());
        assertTrue(detail.overallScore() >= 0);
    }

    @Test
    void getDepartmentDetail_shouldHaveMetrics() {
        var detail = engine.getDepartmentDetail("Marketing");
        assertNotNull(detail.metrics());
        assertFalse(detail.metrics().isEmpty());
    }

    @Test
    void getCrossDepartmentalMetrics_shouldReturnMetrics() {
        var metrics = engine.getCrossDepartmentalMetrics();
        assertNotNull(metrics);
        assertFalse(metrics.isEmpty());
        assertTrue(metrics.size() >= 4);
    }

    @Test
    void getCrossDepartmentalMetrics_shouldHaveRevenueGrowth() {
        var metrics = engine.getCrossDepartmentalMetrics();
        var revenueMetric = metrics.stream()
            .filter(m -> m.name().equals("Overall Revenue Growth"))
            .findFirst();
        assertTrue(revenueMetric.isPresent());
        assertEquals(13.6, revenueMetric.get().current());
    }

    @Test
    void getCrossDepartmentalMetrics_shouldHaveStatus() {
        var metrics = engine.getCrossDepartmentalMetrics();
        for (var metric : metrics) {
            assertNotNull(metric.status());
        }
    }

    @Test
    void departmentSummaries_shouldHaveAllFields() {
        var request = new PerformanceRequest(null, "current_quarter");
        var response = engine.analyzePerformance(request);
        for (var dept : response.departments()) {
            assertNotNull(dept.name());
            assertTrue(dept.score() >= 0);
            assertNotNull(dept.strengths());
            assertNotNull(dept.improvements());
        }
    }
}
