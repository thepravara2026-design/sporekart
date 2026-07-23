package com.sporekart.trainer.copilot.engine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CertificationEngine {

    private static final Logger log = LoggerFactory.getLogger(CertificationEngine.class);

    private static final Random RANDOM = new Random(42);

    private static final double MIN_ATTENDANCE = 80.0;
    private static final double MIN_ASSESSMENT_SCORE = 60.0;
    private static final double MIN_PRACTICAL_COMPLETION = 80.0;
    private static final double PASS_THRESHOLD = 70.0;
    private static final double NEEDS_IMPROVEMENT_THRESHOLD = 50.0;

    private static final String CERTIFICATION_ISSUER = "SporeKart Training Academy";
    private static final String CERTIFICATION_TITLE = "Certified Mushroom Cultivation Specialist";

    private final Map<String, List<CertificationRecord>> seedCertificationData;

    public CertificationEngine() {
        this.seedCertificationData = generateSeedData();
        log.info("CertificationEngine initialized with {} certification records",
                seedCertificationData.values().stream().mapToInt(List::size).sum());
    }

    public Map<String, Object> evaluateEligibility(String studentId, String batchId) {
        log.info("Evaluating eligibility for studentId='{}', batchId='{}'", studentId, batchId);

        CertificationRecord record = findRecord(studentId, batchId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("studentId", studentId);
        result.put("batchId", batchId);
        result.put("studentName", record != null ? record.studentName : "Unknown");

        if (record == null) {
            result.put("eligible", false);
            result.put("reason", "Student record not found");
            log.warn("Student record not found for studentId='{}'", studentId);
            return result;
        }

        boolean attendanceEligible = record.attendance >= MIN_ATTENDANCE;
        boolean assessmentEligible = record.assessmentScore >= MIN_ASSESSMENT_SCORE;
        boolean practicalEligible = record.practicalCompletion >= MIN_PRACTICAL_COMPLETION;

        boolean eligible = attendanceEligible && assessmentEligible && practicalEligible;

        result.put("eligible", eligible);
        result.put("attendance", Math.round(record.attendance * 10.0) / 10.0);
        result.put("attendanceEligible", attendanceEligible);
        result.put("attendanceRequirement", MIN_ATTENDANCE + "%");
        result.put("assessmentScore", Math.round(record.assessmentScore * 10.0) / 10.0);
        result.put("assessmentEligible", assessmentEligible);
        result.put("assessmentRequirement", MIN_ASSESSMENT_SCORE + "%");
        result.put("practicalCompletion", Math.round(record.practicalCompletion * 10.0) / 10.0);
        result.put("practicalEligible", practicalEligible);
        result.put("practicalRequirement", MIN_PRACTICAL_COMPLETION + "%");

        if (!eligible) {
            List<String> deficits = new ArrayList<>();
            if (!attendanceEligible) deficits.add("Attendance below " + MIN_ATTENDANCE + "%");
            if (!assessmentEligible) deficits.add("Assessment score below " + MIN_ASSESSMENT_SCORE + "%");
            if (!practicalEligible) deficits.add("Practical completion below " + MIN_PRACTICAL_COMPLETION + "%");
            result.put("deficits", deficits);
            result.put("recommendation", "Address deficits: " + String.join(", ", deficits));
        } else {
            result.put("recommendation", "Eligible for certification. Proceed to certificate generation.");
        }

        log.info("Eligibility result for {}: eligible={}", studentId, eligible);
        return result;
    }

    public Map<String, Object> calculateOverallScore(StudentProgress progress) {
        log.info("Calculating overall score for studentId='{}'", progress.studentId());

        double attendanceWeight = 0.2;
        double assessmentWeight = 0.5;
        double practicalWeight = 0.3;

        double attendance = progress.overallAttendance();
        double assessment = progress.overallAssessmentScore();
        double practical = progress.practicalCompletionRate();

        double overallScore = attendance * attendanceWeight + assessment * assessmentWeight + practical * practicalWeight;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("studentId", progress.studentId());
        result.put("studentName", progress.studentName());
        result.put("attendance", Math.round(attendance * 10.0) / 10.0);
        result.put("attendanceWeight", attendanceWeight);
        result.put("attendanceContribution", Math.round(attendance * attendanceWeight * 10.0) / 10.0);
        result.put("assessmentScore", Math.round(assessment * 10.0) / 10.0);
        result.put("assessmentWeight", assessmentWeight);
        result.put("assessmentContribution", Math.round(assessment * assessmentWeight * 10.0) / 10.0);
        result.put("practicalCompletion", Math.round(practical * 10.0) / 10.0);
        result.put("practicalWeight", practicalWeight);
        result.put("practicalContribution", Math.round(practical * practicalWeight * 10.0) / 10.0);
        result.put("overallScore", Math.round(overallScore * 10.0) / 10.0);
        result.put("formula", "overall = attendance*0.2 + assessment*0.5 + practical*0.3");

        log.info("Overall score calculated: {} (attendance={}, assessment={}, practical={})",
                Math.round(overallScore), Math.round(attendance), Math.round(assessment), Math.round(practical));
        return result;
    }

    public Map<String, Object> generateRecommendation(double overallScore, double attendance, double practicalCompletion) {
        log.info("Generating recommendation: overallScore={}, attendance={}, practicalCompletion={}",
                overallScore, attendance, practicalCompletion);

        String recommendation;
        String grade;
        List<String> requirements;

        if (overallScore >= PASS_THRESHOLD && attendance >= MIN_ATTENDANCE && practicalCompletion >= MIN_PRACTICAL_COMPLETION) {
            recommendation = "Pass";
            grade = "Certified Mushroom Cultivation Specialist";
            requirements = List.of("All certification requirements met");
        } else if (overallScore >= NEEDS_IMPROVEMENT_THRESHOLD) {
            recommendation = "Needs Improvement";
            grade = "Provisional Certification";
            requirements = new ArrayList<>();
            if (attendance < MIN_ATTENDANCE) requirements.add("Improve attendance to >= " + MIN_ATTENDANCE + "%");
            if (practicalCompletion < MIN_PRACTICAL_COMPLETION) requirements.add("Complete pending practicals to >= " + MIN_PRACTICAL_COMPLETION + "%");
            if (overallScore < PASS_THRESHOLD) requirements.add("Improve overall score to >= " + PASS_THRESHOLD + "%");
        } else {
            recommendation = "Additional Training Required";
            grade = "Not Certified";
            requirements = List.of("Requires additional training and re-assessment");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("overallScore", Math.round(overallScore * 10.0) / 10.0);
        result.put("recommendation", recommendation);
        result.put("grade", grade);
        result.put("passThreshold", PASS_THRESHOLD + "%");
        result.put("requirements", requirements);

        log.info("Recommendation: {} (score={})", recommendation, Math.round(overallScore));
        return result;
    }

    public Map<String, Object> generateCertificate(Certification certification) {
        log.info("Generating certificate for studentId='{}'", certification.studentId());

        String certificateId = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDate issueDate = LocalDate.now();
        LocalDate expiryDate = issueDate.plusYears(2);

        double overallScore = certification.overallScore();

        Map<String, Object> certificate = new LinkedHashMap<>();
        certificate.put("certificateId", certificateId);
        certificate.put("studentId", certification.studentId());
        certificate.put("studentName", certification.studentName());
        certificate.put("courseName", certification.courseName());
        certificate.put("batchId", certification.batchId());
        certificate.put("title", CERTIFICATION_TITLE);
        certificate.put("issuer", CERTIFICATION_ISSUER);
        certificate.put("issueDate", issueDate.toString());
        certificate.put("expiryDate", expiryDate.toString());
        certificate.put("overallScore", Math.round(overallScore * 10.0) / 10.0);
        certificate.put("grade", getGrade(overallScore));
        certificate.put("status", overallScore >= PASS_THRESHOLD ? "Active" : "Conditional");

        List<Map<String, Object>> metrics = new ArrayList<>();
        metrics.add(Map.of("metric", "Attendance", "score", Math.round(certification.attendance() * 10.0) / 10.0, "weight", "20%"));
        metrics.add(Map.of("metric", "Assessment", "score", Math.round(certification.assessmentScore() * 10.0) / 10.0, "weight", "50%"));
        metrics.add(Map.of("metric", "Practical", "score", Math.round(certification.practicalCompletion() * 10.0) / 10.0, "weight", "30%"));
        certificate.put("performanceMetrics", metrics);

        certificate.put("certificateUrl", "/api/certificates/" + certificateId + "/download");
        certificate.put("verificationUrl", "/api/certificates/" + certificateId + "/verify");

        log.info("Certificate generated: {}", certificateId);
        return certificate;
    }

    public Map<String, Object> getBatchCertificationSummary(String batchId) {
        log.info("Generating certification summary for batchId='{}'", batchId);

        List<CertificationRecord> records = seedCertificationData.getOrDefault(batchId, Collections.emptyList());

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("batchId", batchId);
        summary.put("totalStudents", records.size());
        summary.put("generatedAt", LocalDateTime.now().toString());

        int passed = 0;
        int needsImprovement = 0;
        int additionalTraining = 0;

        List<Map<String, Object>> studentResults = new ArrayList<>();
        for (CertificationRecord r : records) {
            double overallScore = r.attendance * 0.2 + r.assessmentScore * 0.5 + r.practicalCompletion * 0.3;
            String status;
            if (overallScore >= PASS_THRESHOLD && r.attendance >= MIN_ATTENDANCE && r.practicalCompletion >= MIN_PRACTICAL_COMPLETION) {
                status = "Pass";
                passed++;
            } else if (overallScore >= NEEDS_IMPROVEMENT_THRESHOLD) {
                status = "Needs Improvement";
                needsImprovement++;
            } else {
                status = "Additional Training";
                additionalTraining++;
            }

            Map<String, Object> student = new LinkedHashMap<>();
            student.put("studentId", r.studentId);
            student.put("studentName", r.studentName);
            student.put("attendance", Math.round(r.attendance * 10.0) / 10.0);
            student.put("assessmentScore", Math.round(r.assessmentScore * 10.0) / 10.0);
            student.put("practicalCompletion", Math.round(r.practicalCompletion * 10.0) / 10.0);
            student.put("overallScore", Math.round(overallScore * 10.0) / 10.0);
            student.put("status", status);
            studentResults.add(student);
        }

        summary.put("studentResults", studentResults);
        summary.put("passed", passed);
        summary.put("needsImprovement", needsImprovement);
        summary.put("additionalTraining", additionalTraining);
        summary.put("passRate", records.isEmpty() ? 0.0 : Math.round((double) passed / records.size() * 100 * 10.0) / 10.0);

        log.info("Batch summary: passRate={}% ({} passed, {} needs improvement, {} additional training)",
                summary.get("passRate"), passed, needsImprovement, additionalTraining);
        return summary;
    }

    private CertificationRecord findRecord(String studentId, String batchId) {
        List<CertificationRecord> batch = seedCertificationData.get(batchId);
        if (batch != null) {
            for (CertificationRecord r : batch) {
                if (r.studentId.equals(studentId)) {
                    return r;
                }
            }
        }
        for (List<CertificationRecord> records : seedCertificationData.values()) {
            for (CertificationRecord r : records) {
                if (r.studentId.equals(studentId)) {
                    return r;
                }
            }
        }
        return null;
    }

    private String getGrade(double overallScore) {
        if (overallScore >= 90) return "A - Outstanding";
        if (overallScore >= 80) return "B - Excellent";
        if (overallScore >= 70) return "C - Good";
        if (overallScore >= 60) return "D - Satisfactory";
        return "F - Needs Improvement";
    }

    private Map<String, List<CertificationRecord>> generateSeedData() {
        Map<String, List<CertificationRecord>> data = new LinkedHashMap<>();

        String[][] studentData = {
            {"STU-001", "Aarav Sharma", "BATCH-MC-2026-001"},
            {"STU-002", "Priya Patel", "BATCH-MC-2026-001"},
            {"STU-003", "Ravi Kumar", "BATCH-MC-2026-001"},
            {"STU-004", "Sunita Verma", "BATCH-MC-2026-001"},
            {"STU-005", "Vikram Singh", "BATCH-MC-2026-001"},
            {"STU-006", "Ananya Gupta", "BATCH-MC-2026-001"},
            {"STU-007", "Deepak Joshi", "BATCH-MC-2026-001"},
            {"STU-008", "Kavita Rao", "BATCH-MC-2026-002"},
            {"STU-009", "Rajesh Nair", "BATCH-MC-2026-002"},
            {"STU-010", "Meena Iyer", "BATCH-MC-2026-002"},
            {"STU-011", "Arjun Reddy", "BATCH-MC-2026-002"},
            {"STU-012", "Lakshmi Menon", "BATCH-MC-2026-002"},
            {"STU-013", "Suresh Desai", "BATCH-MC-2026-002"},
            {"STU-014", "Pooja Malhotra", "BATCH-MC-2026-002"},
            {"STU-015", "Gaurav Khanna", "BATCH-MC-2026-003"},
            {"STU-016", "Nisha Agarwal", "BATCH-MC-2026-003"},
            {"STU-017", "Rohit Choudhary", "BATCH-MC-2026-003"},
            {"STU-018", "Sneha Kapoor", "BATCH-MC-2026-003"},
            {"STU-019", "Amit Saxena", "BATCH-MC-2026-003"},
            {"STU-020", "Divya Bhatia", "BATCH-MC-2026-003"}
        };

        for (String batchId : List.of("BATCH-MC-2026-001", "BATCH-MC-2026-002", "BATCH-MC-2026-003")) {
            List<CertificationRecord> records = new ArrayList<>();
            for (String[] s : studentData) {
                if (s[2].equals(batchId)) {
                    double attendance = clamp(60 + RANDOM.nextDouble() * 40);
                    double assessment = clamp(45 + RANDOM.nextDouble() * 50);
                    double practical = clamp(55 + RANDOM.nextDouble() * 45);
                    records.add(new CertificationRecord(s[0], s[1], batchId, attendance, assessment, practical));
                }
            }
            data.put(batchId, records);
        }

        return data;
    }

    private static double clamp(double value) {
        return Math.min(100, Math.max(0, value));
    }

    public record CertificationRecord(
        String studentId,
        String studentName,
        String batchId,
        double attendance,
        double assessmentScore,
        double practicalCompletion
    ) {}

    public record Certification(
        String studentId,
        String studentName,
        String courseName,
        String batchId,
        double overallScore,
        double attendance,
        double assessmentScore,
        double practicalCompletion
    ) {}
}
