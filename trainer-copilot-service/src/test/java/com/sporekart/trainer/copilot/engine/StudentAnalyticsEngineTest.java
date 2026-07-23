package com.sporekart.trainer.copilot.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentAnalyticsEngineTest {

    private StudentAnalyticsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new StudentAnalyticsEngine();
    }

    @Test
    void getAttendanceTrendsReturnsMonthlyData() {
        Map<String, Object> trends = engine.getAttendanceTrends("B001");

        assertNotNull(trends.get("batchId"));
        assertNotNull(trends.get("trends"));
        List<Map<String, Object>> trendData = (List<Map<String, Object>>) trends.get("trends");
        assertFalse(trendData.isEmpty());
    }

    @Test
    void getPerformanceTrendsReturnsScoreProgression() {
        Map<String, Object> trends = engine.getPerformanceTrends("B001");

        assertNotNull(trends.get("batchId"));
        assertNotNull(trends.get("averageScores"));
        assertNotNull(trends.get("highestScores"));
    }

    @Test
    void identifyWeakTopicsReturnsTopics() {
        List<String> weakTopics = engine.identifyWeakTopics("S001");

        assertNotNull(weakTopics);
        assertFalse(weakTopics.isEmpty());
    }

    @Test
    void identifyStrongTopicsReturnsTopics() {
        List<String> strongTopics = engine.identifyStrongTopics("S001");

        assertNotNull(strongTopics);
        assertFalse(strongTopics.isEmpty());
    }

    @Test
    void getCompletionStatusReturnsDetailedStatus() {
        Map<String, Object> status = engine.getCompletionStatus("S001", "B001");

        assertNotNull(status.get("studentId"));
        assertNotNull(status.get("overallProgress"));
        assertNotNull(status.get("modules"));
    }

    @Test
    void getAssignmentProgressReturnsPercentages() {
        Map<String, Object> progress = engine.getAssignmentProgress("S001");

        assertNotNull(progress.get("completed"));
        assertNotNull(progress.get("total"));
        assertNotNull(progress.get("percentage"));
    }

    @Test
    void getPracticalCompletionReturnsStats() {
        Map<String, Object> completion = engine.getPracticalCompletion("S001");

        assertNotNull(completion.get("completedPracticals"));
        assertNotNull(completion.get("totalPracticals"));
        assertNotNull(completion.get("completionRate"));
    }

    @Test
    void getCertificationReadinessReturnsAssessment() {
        Map<String, Object> readiness = engine.getCertificationReadiness("S001", "B001");

        assertNotNull(readiness.get("eligible"));
        assertNotNull(readiness.get("readinessScore"));
        assertNotNull(readiness.get("missingRequirements"));
    }

    @Test
    void getLearningRecommendationsReturnsSuggestions() {
        List<Map<String, Object>> recommendations = engine.getLearningRecommendations("S001");

        assertFalse(recommendations.isEmpty());
        for (Map<String, Object> rec : recommendations) {
            assertNotNull(rec.get("topic"));
            assertNotNull(rec.get("priority"));
            assertNotNull(rec.get("action"));
        }
    }

    @Test
    void getAttendanceTrendsForDifferentBatches() {
        Map<String, Object> trendsA = engine.getAttendanceTrends("B001");
        Map<String, Object> trendsB = engine.getAttendanceTrends("B002");

        assertEquals("B001", trendsA.get("batchId"));
        assertEquals("B002", trendsB.get("batchId"));
    }

    @Test
    void getCompletionStatusForNonExistentStudent() {
        Map<String, Object> status = engine.getCompletionStatus("INVALID", "B001");

        assertNotNull(status.get("overallProgress"));
    }
}
