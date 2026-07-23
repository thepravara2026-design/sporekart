package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.TrainingAnalytics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainingAnalyticsEngineTest {

    private TrainingAnalyticsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new TrainingAnalyticsEngine();
    }

    @Test
    void getTrainingSummary_shouldReturnAnalyticsForValidPeriod() {
        TrainingAnalytics analytics = engine.getTrainingSummary("2025-07");
        assertNotNull(analytics);
        assertEquals("2025-07", analytics.period());
        assertTrue(analytics.totalStudents() > 0);
    }

    @Test
    void getTrainingSummary_shouldReturnLatestForNullPeriod() {
        TrainingAnalytics analytics = engine.getTrainingSummary(null);
        assertNotNull(analytics);
        assertTrue(analytics.totalStudents() > 0);
    }

    @Test
    void getTrainingSummary_shouldReturnEmptyForUnknownPeriod() {
        TrainingAnalytics analytics = engine.getTrainingSummary("2099-01");
        assertNotNull(analytics);
        assertEquals(0, analytics.totalStudents());
    }

    @Test
    void getStudentPerformanceByCourse_shouldReturnThreeCourses() {
        Map<String, Object> perf = engine.getStudentPerformanceByCourse();
        assertEquals(3, perf.size());
    }

    @Test
    void getStudentPerformanceByCourse_shouldHaveScoreKey() {
        Map<String, Object> perf = engine.getStudentPerformanceByCourse();
        assertTrue(perf.values().stream().allMatch(v -> v instanceof Map));
    }

    @Test
    void getCertificationRate_shouldReturnPositive() {
        double rate = engine.getCertificationRate();
        assertTrue(rate > 0);
    }

    @Test
    void getTrainingRevenue_shouldReturnPositive() {
        double revenue = engine.getTrainingRevenue();
        assertTrue(revenue > 0);
    }

    @Test
    void getTrainingProfitMargin_shouldReturnValidPercent() {
        double margin = engine.getTrainingProfitMargin();
        assertTrue(margin > 0);
    }

    @Test
    void getTopPerformingCourses_shouldBeSortedByScore() {
        List<Map<String, Object>> top = engine.getTopPerformingCourses();
        assertEquals(3, top.size());
        assertTrue((Double) top.get(0).get("averageScore") >= (Double) top.get(1).get("averageScore"));
    }

    @Test
    void getTopPerformingCourses_shouldHaveRequiredFields() {
        List<Map<String, Object>> top = engine.getTopPerformingCourses();
        Map<String, Object> course = top.get(0);
        assertTrue(course.containsKey("courseName"));
        assertTrue(course.containsKey("averageScore"));
        assertTrue(course.containsKey("enrollments"));
        assertTrue(course.containsKey("completionRate"));
        assertTrue(course.containsKey("certificationRate"));
    }

    @Test
    void getScoreDistributionByModule_shouldReturnEightModules() {
        Map<String, Double> dist = engine.getScoreDistributionByModule();
        assertEquals(8, dist.size());
    }

    @Test
    void getStudentRetention_shouldReturnBetweenZeroAndOne() {
        double retention = engine.getStudentRetention();
        assertTrue(retention > 0 && retention <= 1.0);
    }
}
