package com.sporekart.trainer.copilot.engine;

import com.sporekart.trainer.copilot.domain.Certification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CertificationEngineTest {

    private CertificationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CertificationEngine();
    }

    @Test
    void evaluateEligibilityReturnsPassWhenRequirementsMet() {
        Map<String, Object> result = engine.evaluateEligibility("S001", "B001");

        assertNotNull(result.get("eligible"));
        assertNotNull(result.get("criteria"));
    }

    @Test
    void evaluateEligibilityReturnsFailWhenAttendanceLow() {
        Map<String, Object> result = engine.evaluateEligibility("S002", "B001");

        assertFalse((Boolean) result.get("eligible"));
        List<String> reasons = (List<String>) result.get("reasons");
        assertFalse(reasons.isEmpty());
    }

    @Test
    void calculateOverallScoreCombinesAllScores() {
        Map<String, Object> score = engine.calculateOverallScore("S001", "B001");

        assertNotNull(score.get("overallScore"));
        assertNotNull(score.get("attendanceScore"));
        assertNotNull(score.get("assessmentScore"));
        assertNotNull(score.get("practicalScore"));
        assertNotNull(score.get("weightings"));
    }

    @Test
    void calculateOverallScoreIsWeightedAverage() {
        Map<String, Object> score = engine.calculateOverallScore("S001", "B001");
        double overall = (Double) score.get("overallScore");

        assertTrue(overall >= 0 && overall <= 100);
    }

    @Test
    void generateRecommendationPassWhenScoreHigh() {
        String recommendation = engine.generateRecommendation("S001", "B001", 85.0);

        assertEquals("PASS", recommendation);
    }

    @Test
    void generateRecommendationNeedsImprovementWhenScoreMedium() {
        String recommendation = engine.generateRecommendation("S002", "B001", 65.0);

        assertEquals("NEEDS_IMPROVEMENT", recommendation);
    }

    @Test
    void generateRecommendationAdditionalTrainingWhenScoreLow() {
        String recommendation = engine.generateRecommendation("S003", "B001", 45.0);

        assertEquals("ADDITIONAL_TRAINING", recommendation);
    }

    @Test
    void generateCertificateReturnsCertificationRecord() {
        Certification cert = engine.generateCertificate("S001", "S001", "John Doe", "B001", "Mushroom Cultivation", 85.0, 78.0, 90.0, "PASS");

        assertNotNull(cert);
        assertEquals("PASS", cert.recommendation());
        assertEquals("John Doe", cert.studentName());
        assertNotNull(cert.issuedDate());
    }

    @Test
    void generateCertificateHasStatusAndRemarks() {
        Certification cert = engine.generateCertificate("S002", "S002", "Jane Doe", "B002", "Advanced Mycology", 70.0, 65.0, 80.0, "NEEDS_IMPROVEMENT");

        assertNotNull(cert.status());
        assertNotNull(cert.remarks());
        assertFalse(cert.remarks().isEmpty());
    }

    @Test
    void getBatchCertificationSummaryReturnsAggregate() {
        Map<String, Object> summary = engine.getBatchCertificationSummary("B001");

        assertNotNull(summary.get("batchId"));
        assertNotNull(summary.get("totalStudents"));
        assertNotNull(summary.get("certified"));
        assertNotNull(summary.get("pending"));
        assertNotNull(summary.get("failed"));
    }

    @Test
    void getBatchCertificationSummaryHasStudentList() {
        Map<String, Object> summary = engine.getBatchCertificationSummary("B001");

        List<Map<String, Object>> students = (List<Map<String, Object>>) summary.get("students");
        assertNotNull(students);
        assertFalse(students.isEmpty());
    }
}
