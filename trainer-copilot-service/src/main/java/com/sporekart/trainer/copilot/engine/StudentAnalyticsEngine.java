package com.sporekart.trainer.copilot.engine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sporekart.trainer.copilot.dto.AnalyticsSummaryResponse;

@Component
public class StudentAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(StudentAnalyticsEngine.class);

    private static final Random RANDOM = new Random(42);

    private static final List<String> BATCH_IDS = List.of("BATCH-MC-2026-001", "BATCH-MC-2026-002", "BATCH-MC-2026-003");
    private static final List<String> MODULE_NAMES = List.of(
        "Mushroom Biology & Safety",
        "Spawn Preparation",
        "Substrate Formulation",
        "Inoculation & Incubation",
        "Fruiting Management",
        "Disease Control",
        "Harvesting & Post-Harvest",
        "Business & Certification"
    );

    private final Map<String, List<StudentProgress>> studentData;

    public StudentAnalyticsEngine() {
        this.studentData = generateSeedData();
        log.info("StudentAnalyticsEngine initialized with {} students across {} batches",
                studentData.values().stream().mapToInt(List::size).sum(), BATCH_IDS.size());
    }

    public Map<String, Object> getAttendanceTrends(String batchId) {
        log.info("Fetching attendance trends for batchId='{}'", batchId);

        List<StudentProgress> batch = studentData.getOrDefault(batchId, Collections.emptyList());
        Map<String, Object> trends = new LinkedHashMap<>();
        trends.put("batchId", batchId);
        trends.put("totalStudents", batch.size());

        List<Map<String, Object>> weeklyData = new ArrayList<>();
        for (int week = 1; week <= 8; week++) {
            int finalWeek = week;
            double avgAttendance = batch.stream()
                    .mapToDouble(s -> s.weeklyAttendance().getOrDefault(finalWeek, 0.0))
                    .average().orElse(0.0);

            Map<String, Object> weekEntry = new LinkedHashMap<>();
            weekEntry.put("week", week);
            weekEntry.put("averageAttendance", Math.round(avgAttendance * 10.0) / 10.0);
            weekEntry.put("totalSessions", 5);
            weekEntry.put("studentsAbove90", batch.stream().filter(s -> s.weeklyAttendance().getOrDefault(finalWeek, 0.0) >= 90).count());
            weekEntry.put("studentsBelow75", batch.stream().filter(s -> s.weeklyAttendance().getOrDefault(finalWeek, 0.0) < 75).count());
            weeklyData.add(weekEntry);
        }
        trends.put("weeklyData", weeklyData);
        trends.put("overallAverage", Math.round(weeklyData.stream().mapToDouble(w -> (double) w.get("averageAttendance")).average().orElse(0.0) * 10.0) / 10.0);

        log.info("Attendance trends generated for batchId='{}'", batchId);
        return trends;
    }

    public Map<String, Object> getPerformanceTrends(String batchId) {
        log.info("Fetching performance trends for batchId='{}'", batchId);

        List<StudentProgress> batch = studentData.getOrDefault(batchId, Collections.emptyList());
        Map<String, Object> trends = new LinkedHashMap<>();
        trends.put("batchId", batchId);
        trends.put("totalStudents", batch.size());

        List<Map<String, Object>> weeklyData = new ArrayList<>();
        for (int week = 1; week <= 8; week++) {
            int finalWeek = week;
            List<StudentProgress> assessed = batch.stream()
                    .filter(s -> s.assessmentScores().containsKey(finalWeek))
                    .toList();

            double avgScore = assessed.stream()
                    .mapToDouble(s -> s.assessmentScores().get(finalWeek))
                    .average().orElse(0.0);

            Map<String, Object> weekEntry = new LinkedHashMap<>();
            weekEntry.put("week", week);
            weekEntry.put("studentsAssessed", assessed.size());
            weekEntry.put("averageScore", Math.round(avgScore * 10.0) / 10.0);
            weekEntry.put("highestScore", assessed.stream().mapToDouble(s -> s.assessmentScores().get(finalWeek)).max().orElse(0.0));
            weekEntry.put("lowestScore", assessed.stream().mapToDouble(s -> s.assessmentScores().get(finalWeek)).min().orElse(0.0));
            weeklyData.add(weekEntry);
        }
        trends.put("weeklyPerformance", weeklyData);
        trends.put("overallAverage", Math.round(weeklyData.stream().mapToDouble(w -> (double) w.get("averageScore")).average().orElse(0.0) * 10.0) / 10.0);

        log.info("Performance trends generated for batchId='{}'", batchId);
        return trends;
    }

    public List<Map<String, Object>> identifyWeakTopics(String studentId) {
        log.info("Identifying weak topics for studentId='{}'", studentId);

        StudentProgress student = findStudent(studentId);
        if (student == null) {
            log.warn("Student not found: {}", studentId);
            return List.of();
        }

        List<Map<String, Object>> weakTopics = new ArrayList<>();
        for (int i = 0; i < MODULE_NAMES.size(); i++) {
            double score = student.moduleScores().getOrDefault(i + 1, 50.0);
            if (score < 70.0) {
                Map<String, Object> topic = new LinkedHashMap<>();
                topic.put("moduleId", i + 1);
                topic.put("moduleName", MODULE_NAMES.get(i));
                topic.put("score", Math.round(score * 10.0) / 10.0);
                topic.put("performanceLevel", score < 50 ? "Critical" : "Needs Improvement");
                topic.put("recommendedAction", getWeakTopicRecommendation(i, score));
                weakTopics.add(topic);
            }
        }
        weakTopics.sort((a, b) -> Double.compare((double) a.get("score"), (double) b.get("score")));

        log.info("Found {} weak topics for studentId='{}'", weakTopics.size(), studentId);
        return weakTopics;
    }

    public List<Map<String, Object>> identifyStrongTopics(String studentId) {
        log.info("Identifying strong topics for studentId='{}'", studentId);

        StudentProgress student = findStudent(studentId);
        if (student == null) {
            log.warn("Student not found: {}", studentId);
            return List.of();
        }

        List<Map<String, Object>> strongTopics = new ArrayList<>();
        for (int i = 0; i < MODULE_NAMES.size(); i++) {
            double score = student.moduleScores().getOrDefault(i + 1, 50.0);
            if (score >= 80.0) {
                Map<String, Object> topic = new LinkedHashMap<>();
                topic.put("moduleId", i + 1);
                topic.put("moduleName", MODULE_NAMES.get(i));
                topic.put("score", Math.round(score * 10.0) / 10.0);
                topic.put("performanceLevel", score >= 90 ? "Excellent" : "Good");
                topic.put("recommendation", "Consider peer mentoring in " + MODULE_NAMES.get(i));
                strongTopics.add(topic);
            }
        }
        strongTopics.sort((a, b) -> Double.compare((double) b.get("score"), (double) a.get("score")));

        log.info("Found {} strong topics for studentId='{}'", strongTopics.size(), studentId);
        return strongTopics;
    }

    public Map<String, Object> getCompletionStatus(String batchId) {
        log.info("Fetching completion status for batchId='{}'", batchId);

        List<StudentProgress> batch = studentData.getOrDefault(batchId, Collections.emptyList());
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("batchId", batchId);
        status.put("totalStudents", batch.size());

        List<Map<String, Object>> moduleProgress = new ArrayList<>();
        for (int i = 0; i < MODULE_NAMES.size(); i++) {
            int moduleNumber = i + 1;
            long completed = batch.stream().filter(s -> s.completedModules().contains(moduleNumber)).count();
            double percentage = batch.isEmpty() ? 0 : (double) completed / batch.size() * 100;

            Map<String, Object> module = new LinkedHashMap<>();
            module.put("moduleId", moduleNumber);
            module.put("moduleName", MODULE_NAMES.get(i));
            module.put("completedCount", completed);
            module.put("totalCount", batch.size());
            module.put("completionPercentage", Math.round(percentage * 10.0) / 10.0);
            moduleProgress.add(module);
        }
        status.put("moduleCompletion", moduleProgress);
        status.put("overallCompletion", Math.round(moduleProgress.stream().mapToDouble(m -> (double) m.get("completionPercentage")).average().orElse(0.0) * 10.0) / 10.0);

        log.info("Completion status generated for batchId='{}'", batchId);
        return status;
    }

    public Map<String, Object> getAssignmentProgress(String batchId) {
        log.info("Fetching assignment progress for batchId='{}'", batchId);

        List<StudentProgress> batch = studentData.getOrDefault(batchId, Collections.emptyList());
        int totalAssignments = 8;

        Map<String, Object> progress = new LinkedHashMap<>();
        progress.put("batchId", batchId);
        progress.put("totalAssignments", totalAssignments);

        List<Map<String, Object>> studentList = new ArrayList<>();
        for (StudentProgress s : batch) {
            int submitted = s.completedAssignments().size();
            int graded = (int) s.completedAssignments().stream().filter(a -> s.assessmentScores().containsKey(a)).count();
            double avgScore = submitted > 0 ? s.completedAssignments().stream()
                    .filter(a -> s.assessmentScores().containsKey(a))
                    .mapToDouble(a -> s.assessmentScores().get(a))
                    .average().orElse(0.0) : 0.0;

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("studentId", s.studentId());
            entry.put("studentName", s.studentName());
            entry.put("submitted", submitted);
            entry.put("graded", graded);
            entry.put("pending", submitted - graded);
            entry.put("averageScore", Math.round(avgScore * 10.0) / 10.0);
            studentList.add(entry);
        }
        progress.put("studentProgress", studentList);

        double overallSubmission = batch.isEmpty() ? 0 : (double) batch.stream().mapToInt(s -> s.completedAssignments().size()).sum() / (batch.size() * totalAssignments) * 100;
        progress.put("overallSubmissionRate", Math.round(overallSubmission * 10.0) / 10.0);

        log.info("Assignment progress generated for batchId='{}'", batchId);
        return progress;
    }

    public Map<String, Object> getPracticalCompletion(String batchId) {
        log.info("Fetching practical completion for batchId='{}'", batchId);

        List<StudentProgress> batch = studentData.getOrDefault(batchId, Collections.emptyList());
        int totalPracticals = 8;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("batchId", batchId);
        result.put("totalPracticals", totalPracticals);

        List<Map<String, Object>> studentList = new ArrayList<>();
        for (StudentProgress s : batch) {
            int completed = s.completedPracticals().size();
            double rate = (double) completed / totalPracticals * 100;

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("studentId", s.studentId());
            entry.put("studentName", s.studentName());
            entry.put("completed", completed);
            entry.put("total", totalPracticals);
            entry.put("completionRate", Math.round(rate * 10.0) / 10.0);
            studentList.add(entry);
        }
        result.put("studentProgress", studentList);

        double overall = batch.isEmpty() ? 0 : (double) batch.stream().mapToInt(s -> s.completedPracticals().size()).sum() / (batch.size() * totalPracticals) * 100;
        result.put("overallCompletionRate", Math.round(overall * 10.0) / 10.0);

        log.info("Practical completion generated for batchId='{}'", batchId);
        return result;
    }

    public Map<String, Object> getCertificationReadiness(List<StudentProgress> students) {
        log.info("Evaluating certification readiness for {} students", students.size());

        Map<String, Object> readiness = new LinkedHashMap<>();
        readiness.put("totalStudents", students.size());

        List<Map<String, Object>> studentReadiness = new ArrayList<>();
        int ready = 0;
        int borderline = 0;
        int notReady = 0;

        for (StudentProgress s : students) {
            boolean attendanceOk = s.overallAttendance() >= 80.0;
            boolean assessmentOk = s.overallAssessmentScore() >= 60.0;
            boolean practicalOk = s.practicalCompletionRate() >= 80.0;

            String status;
            if (attendanceOk && assessmentOk && practicalOk) {
                status = "Ready";
                ready++;
            } else if (attendanceOk && assessmentOk) {
                status = "Nearly Ready - needs practical completion";
                borderline++;
            } else {
                status = "Not Ready";
                notReady++;
            }

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("studentId", s.studentId());
            entry.put("studentName", s.studentName());
            entry.put("attendance", Math.round(s.overallAttendance() * 10.0) / 10.0);
            entry.put("assessmentScore", Math.round(s.overallAssessmentScore() * 10.0) / 10.0);
            entry.put("practicalCompletion", Math.round(s.practicalCompletionRate() * 10.0) / 10.0);
            entry.put("status", status);
            studentReadiness.add(entry);
        }
        readiness.put("students", studentReadiness);
        readiness.put("readyCount", ready);
        readiness.put("borderlineCount", borderline);
        readiness.put("notReadyCount", notReady);
        readiness.put("readyPercentage", Math.round((double) ready / students.size() * 100 * 10.0) / 10.0);

        log.info("Certification readiness: {} ready, {} borderline, {} not ready", ready, borderline, notReady);
        return readiness;
    }

    public AnalyticsSummaryResponse getSummary() {
        log.info("Generating analytics summary");
        return new AnalyticsSummaryResponse(0, 0, 0, 0, 0.0, 0.0, 0, 0, 0, Map.of());
    }

    public Map<String, Object> getLearningRecommendations(String studentId) {
        log.info("Generating learning recommendations for studentId='{}'", studentId);

        StudentProgress student = findStudent(studentId);
        if (student == null) {
            log.warn("Student not found: {}", studentId);
            Map<String, Object> empty = new LinkedHashMap<>();
            empty.put("studentId", studentId);
            empty.put("error", "Student not found");
            return empty;
        }

        Map<String, Object> recommendations = new LinkedHashMap<>();
        recommendations.put("studentId", student.studentId());
        recommendations.put("studentName", student.studentName());

        List<Map<String, Object>> weakTopics = identifyWeakTopics(studentId);
        List<Map<String, Object>> strongTopics = identifyStrongTopics(studentId);

        List<String> recommendedActions = new ArrayList<>();
        for (Map<String, Object> weak : weakTopics) {
            recommendedActions.add((String) weak.get("recommendedAction"));
        }

        if (student.overallAttendance() < 80.0) {
            recommendedActions.add("Improve attendance - review missed sessions via recordings");
        }
        if (student.overallAssessmentScore() < 60.0) {
            recommendedActions.add("Focus on assessment preparation - use practice quizzes");
        }
        if (student.practicalCompletionRate() < 80.0) {
            recommendedActions.add("Complete pending practical exercises in open lab hours");
        }

        if (!strongTopics.isEmpty()) {
            recommendedActions.add("Consider becoming a peer mentor for " +
                strongTopics.stream().map(t -> (String) t.get("moduleName")).limit(2).collect(Collectors.joining(", ")));
        }

        recommendations.put("weakTopics", weakTopics);
        recommendations.put("strongTopics", strongTopics);
        recommendations.put("recommendedActions", recommendedActions);

        List<String> resources = new ArrayList<>();
        resources.add("Video recordings of weak topic lectures");
        resources.add("Practice question bank for " + (weakTopics.isEmpty() ? "all modules" : weakTopics.stream().map(t -> (String) t.get("moduleName")).collect(Collectors.joining(", "))));
        resources.add("One-on-one tutoring session available upon request");
        resources.add("Peer study group - contact your batch coordinator");
        recommendations.put("recommendedResources", resources);

        log.info("Learning recommendations generated with {} actions", recommendedActions.size());
        return recommendations;
    }

    // ---- Seed data ----

    private Map<String, List<StudentProgress>> generateSeedData() {
        Map<String, List<StudentProgress>> data = new LinkedHashMap<>();

        String[][] studentNames = {
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

        for (String batchId : BATCH_IDS) {
            List<StudentProgress> batchList = new ArrayList<>();
            for (String[] s : studentNames) {
                if (s[2].equals(batchId)) {
                    batchList.add(generateStudentProgress(s[0], s[1], batchId));
                }
            }
            data.put(batchId, batchList);
        }

        return data;
    }

    private StudentProgress generateStudentProgress(String studentId, String name, String batchId) {
        Map<Integer, Double> weeklyAttendance = new LinkedHashMap<>();
        for (int w = 1; w <= 8; w++) {
            weeklyAttendance.put(w, 60.0 + RANDOM.nextDouble() * 40.0);
        }

        Map<Integer, Double> assessmentScores = new LinkedHashMap<>();
        for (int w = 1; w <= 8; w++) {
            if (RANDOM.nextDouble() > 0.1) {
                assessmentScores.put(w, 40.0 + RANDOM.nextDouble() * 60.0);
            }
        }

        Map<Integer, Double> moduleScores = new LinkedHashMap<>();
        for (int m = 1; m <= MODULE_NAMES.size(); m++) {
            moduleScores.put(m, 45.0 + RANDOM.nextDouble() * 55.0);
        }

        List<Integer> completedModules = new ArrayList<>();
        for (int m = 1; m <= MODULE_NAMES.size(); m++) {
            if (RANDOM.nextDouble() > 0.15) {
                completedModules.add(m);
            }
        }

        List<Integer> completedAssignments = new ArrayList<>();
        for (int a = 1; a <= 8; a++) {
            if (RANDOM.nextDouble() > 0.1) {
                completedAssignments.add(a);
            }
        }

        List<Integer> completedPracticals = new ArrayList<>();
        for (int p = 1; p <= 8; p++) {
            if (RANDOM.nextDouble() > 0.2) {
                completedPracticals.add(p);
            }
        }

        double overallAttendance = weeklyAttendance.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double overallAssessment = assessmentScores.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double practicalRate = (double) completedPracticals.size() / 8 * 100;

        return new StudentProgress(
            studentId, name, batchId,
            weeklyAttendance, assessmentScores, moduleScores,
            completedModules, completedAssignments, completedPracticals,
            overallAttendance, overallAssessment, practicalRate
        );
    }

    private StudentProgress findStudent(String studentId) {
        for (List<StudentProgress> batch : studentData.values()) {
            for (StudentProgress s : batch) {
                if (s.studentId().equals(studentId)) {
                    return s;
                }
            }
        }
        return null;
    }

    private String getWeakTopicRecommendation(int moduleIndex, double score) {
        if (score < 50) {
            return "Urgent: Schedule remedial session for " + MODULE_NAMES.get(moduleIndex) + " with instructor";
        }
        return "Review " + MODULE_NAMES.get(moduleIndex) + " materials and attempt supplementary exercises";
    }

    public record StudentProgress(
        String studentId,
        String studentName,
        String batchId,
        Map<Integer, Double> weeklyAttendance,
        Map<Integer, Double> assessmentScores,
        Map<Integer, Double> moduleScores,
        List<Integer> completedModules,
        List<Integer> completedAssignments,
        List<Integer> completedPracticals,
        double overallAttendance,
        double overallAssessmentScore,
        double practicalCompletionRate
    ) {}
}
