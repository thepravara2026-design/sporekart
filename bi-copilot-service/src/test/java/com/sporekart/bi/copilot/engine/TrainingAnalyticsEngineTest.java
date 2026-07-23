package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.TrainingAnalytics;
import com.sporekart.bi.copilot.domain.TrainingAnalytics.TrainerPerformance;
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
    void getTrainingSummaryReturnsValidAnalytics() {
        TrainingAnalytics result = engine.getTrainingSummary("current");
        assertNotNull(result);
        assertTrue(result.totalBatches() >= 0);
        assertTrue(result.totalStudents() >= 0);
        assertTrue(result.averageAttendance() >= 0);
        assertTrue(result.completionRate() >= 0);
        assertNotNull(result.trainerPerformance());
        assertNotNull(result.enrollmentByCourse());
    }

    @Test
    void getBatchPerformanceReturnsBatchDetails() {
        Map<String, Object> result = engine.getBatchPerformance("BATCH0001");
        assertNotNull(result);
        assertTrue(result.containsKey("batchId"));
        assertTrue(result.containsKey("course"));
        assertTrue(result.containsKey("enrolled"));
        assertTrue(result.containsKey("attendanceRate"));
    }

    @Test
    void getBatchPerformanceReturnsErrorForInvalidBatch() {
        Map<String, Object> result = engine.getBatchPerformance("INVALID");
        assertTrue(result.containsKey("error"));
    }

    @Test
    void getEnrollmentByCourseReturnsAllCourses() {
        Map<String, Integer> enrollment = engine.getEnrollmentByCourse("current");
        assertNotNull(enrollment);
        assertTrue(enrollment.containsKey("Mushroom Cultivation 101"));
        assertTrue(enrollment.containsKey("Advanced Oyster Farming"));
    }

    @Test
    void getAttendanceRateReturnsPercentage() {
        double rate = engine.getAttendanceRate("current");
        assertTrue(rate >= 0);
        assertTrue(rate <= 100);
    }

    @Test
    void getCompletionRateReturnsPercentage() {
        double rate = engine.getCompletionRate("current");
        assertTrue(rate >= 0);
        assertTrue(rate <= 100);
    }

    @Test
    void getCertificationRateReturnsPercentage() {
        double rate = engine.getCertificationRate("current");
        assertTrue(rate >= 0);
        assertTrue(rate <= 100);
    }

    @Test
    void getTrainerPerformanceReturnsAllTrainers() {
        List<TrainerPerformance> perf = engine.getTrainerPerformance();
        assertNotNull(perf);
        assertFalse(perf.isEmpty());
    }

    @Test
    void getTopTrainersReturnsOrderedByScore() {
        List<TrainerPerformance> top = engine.getTopTrainers(5);
        assertEquals(5, top.size());
        for (int i = 1; i < top.size(); i++) {
            assertTrue(top.get(i - 1).avgScore() >= top.get(i).avgScore());
        }
    }

    @Test
    void getRevenueByTrainingReturnsRevenueMap() {
        Map<String, Double> revenue = engine.getRevenueByTraining("current");
        assertNotNull(revenue);
        assertFalse(revenue.isEmpty());
    }

    @Test
    void getStudentProgressReturnsFunnelData() {
        Map<String, Object> progress = engine.getStudentProgress("BATCH0001");
        assertNotNull(progress);
        assertTrue(progress.containsKey("funnel"));
    }
}
