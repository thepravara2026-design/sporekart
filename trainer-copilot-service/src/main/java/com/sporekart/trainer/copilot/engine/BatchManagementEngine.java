package com.sporekart.trainer.copilot.engine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BatchManagementEngine {

    private static final Logger log = LoggerFactory.getLogger(BatchManagementEngine.class);

    private static final Random RANDOM = new Random(42);

    private final Map<String, Batch> batchStore;
    private final Map<String, List<Map<String, Object>>> feedbackStore;

    public BatchManagementEngine() {
        this.batchStore = buildBatchStore();
        this.feedbackStore = buildFeedbackStore();
        log.info("BatchManagementEngine initialized with {} batches", batchStore.size());
    }

    public Map<String, Object> getBatchOverview(String batchId) {
        log.info("Fetching overview for batchId='{}'", batchId);
        Batch batch = batchStore.get(batchId);
        if (batch == null) {
            log.warn("Batch not found: {}", batchId);
            return Map.of("error", "Batch not found", "batchId", batchId);
        }
        Map<String, Object> overview = new LinkedHashMap<>();
        overview.put("batchId", batch.batchId);
        overview.put("courseName", batch.courseName);
        overview.put("startDate", batch.startDate.toString());
        overview.put("endDate", batch.endDate.toString());
        overview.put("status", batch.status);
        overview.put("trainerId", batch.trainerId);
        overview.put("trainerName", batch.trainerName);
        overview.put("totalStudents", batch.totalStudents);
        overview.put("currentWeek", batch.currentWeek);
        overview.put("totalWeeks", batch.totalWeeks);
        overview.put("progressPercentage", Math.round((double) batch.currentWeek / batch.totalWeeks * 100 * 10.0) / 10.0);
        List<Map<String, Object>> metrics = new ArrayList<>();
        metrics.add(Map.of("metric", "Average Attendance", "value", batch.avgAttendance + "%", "trend", batch.avgAttendance >= 80 ? "positive" : "negative"));
        metrics.add(Map.of("metric", "Average Assessment", "value", batch.avgAssessmentScore + "%", "trend", batch.avgAssessmentScore >= 60 ? "positive" : "negative"));
        metrics.add(Map.of("metric", "Practical Completion", "value", batch.practicalCompletion + "%", "trend", batch.practicalCompletion >= 70 ? "positive" : "negative"));
        overview.put("metrics", metrics);
        log.info("Batch overview retrieved for '{}'", batchId);
        return overview;
    }

    public List<Map<String, Object>> getUpcomingSessions(String batchId, int days) {
        log.info("Fetching upcoming sessions for batchId='{}', next {} days", batchId, days);
        Batch batch = batchStore.get(batchId);
        if (batch == null) return Collections.emptyList();
        List<Map<String, Object>> sessions = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int d = 0; d < days; d++) {
            LocalDate sessionDate = today.plusDays(d);
            if (sessionDate.getDayOfWeek().getValue() < 6) {
                sessions.add(Map.of(
                    "date", sessionDate.toString(),
                    "dayOfWeek", sessionDate.getDayOfWeek().toString(),
                    "sessionType", d % 2 == 0 ? "Theory" : "Practical Lab",
                    "topic", getSessionTopic(batch, d),
                    "trainer", batch.trainerName,
                    "location", d % 2 == 0 ? "Lecture Hall" : "Cultivation Lab",
                    "status", d == 0 ? "Today" : "Scheduled"
                ));
            }
        }
        return sessions;
    }

    public Map<String, Object> getTrainerSchedule(String trainerId, LocalDate from, LocalDate to) {
        log.info("Fetching schedule for trainerId='{}' from {} to {}", trainerId, from, to);
        List<Map<String, Object>> schedule = new ArrayList<>();
        for (Batch batch : batchStore.values()) {
            if (batch.trainerId.equals(trainerId)) {
                LocalDate start = from.isAfter(batch.startDate) ? from : batch.startDate;
                LocalDate end = to.isBefore(batch.endDate) ? to : batch.endDate;
                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    if (date.getDayOfWeek().getValue() < 6) {
                        schedule.add(Map.of(
                            "date", date.toString(),
                            "batchId", batch.batchId,
                            "courseName", batch.courseName,
                            "sessionType", date.getDayOfWeek().getValue() % 2 == 0 ? "Theory" : "Practical",
                            "location", date.getDayOfWeek().getValue() % 2 == 0 ? "Lecture Hall" : "Cultivation Lab"
                        ));
                    }
                }
            }
        }
        schedule.sort((a, b) -> ((String) a.get("date")).compareTo((String) b.get("date")));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("trainerId", trainerId);
        result.put("fromDate", from.toString());
        result.put("toDate", to.toString());
        result.put("sessions", schedule);
        result.put("totalSessions", schedule.size());
        return result;
    }

    public Map<String, Object> getStudentRoster(String batchId) {
        Batch batch = batchStore.get(batchId);
        if (batch == null) return Map.of("error", "Batch not found");
        List<Map<String, Object>> students = new ArrayList<>();
        for (BatchStudent s : batch.students) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("studentId", s.studentId);
            entry.put("studentName", s.studentName);
            entry.put("email", s.email);
            entry.put("attendance", Math.round(s.attendance * 10.0) / 10.0);
            entry.put("assessmentScore", Math.round(s.assessmentScore * 10.0) / 10.0);
            entry.put("practicalProgress", Math.round(s.practicalProgress * 10.0) / 10.0);
            entry.put("status", s.status);
            entry.put("enrolledDate", s.enrolledDate.toString());
            students.add(entry);
        }
        return Map.of("batchId", batchId, "courseName", batch.courseName, "totalStudents", students.size(), "students", students);
    }

    public Map<String, Object> getBatchCapacity(String batchId) {
        Batch batch = batchStore.get(batchId);
        if (batch == null) return Map.of("error", "Batch not found");
        int available = batch.maxCapacity - batch.totalStudents;
        return Map.of(
            "batchId", batchId, "courseName", batch.courseName,
            "maxCapacity", batch.maxCapacity, "enrolled", batch.totalStudents,
            "available", available,
            "utilizationPercentage", Math.round((double) batch.totalStudents / batch.maxCapacity * 100 * 10.0) / 10.0,
            "status", available > 0 ? "Seats Available" : "Full"
        );
    }

    public Map<String, Object> getAttendanceSummary(String batchId) {
        Batch batch = batchStore.get(batchId);
        if (batch == null) return Map.of("error", "Batch not found");
        List<Map<String, Object>> weeklyData = new ArrayList<>();
        for (int w = 1; w <= batch.currentWeek; w++) {
            weeklyData.add(Map.of(
                "week", w,
                "averageAttendance", Math.round((70 + RANDOM.nextDouble() * 25) * 10.0) / 10.0,
                "above90", (int) (batch.totalStudents * (0.3 + RANDOM.nextDouble() * 0.4)),
                "below75", (int) (batch.totalStudents * (0.05 + RANDOM.nextDouble() * 0.15))
            ));
        }
        return Map.of("batchId", batchId, "overallAverage", batch.avgAttendance, "weeklyData", weeklyData, "weeksCompleted", batch.currentWeek);
    }

    public Map<String, Object> getPracticalCompletion(String batchId) {
        Batch batch = batchStore.get(batchId);
        if (batch == null) return Map.of("error", "Batch not found");
        int totalPracticals = 8;
        List<Map<String, Object>> practicalData = new ArrayList<>();
        for (int p = 1; p <= totalPracticals; p++) {
            practicalData.add(Map.of(
                "practicalNumber", p,
                "practicalName", getPracticalName(p),
                "completedCount", (int) (batch.totalStudents * (0.6 + RANDOM.nextDouble() * 0.4)),
                "completionRate", Math.round((60 + RANDOM.nextDouble() * 35) * 10.0) / 10.0
            ));
        }
        return Map.of("batchId", batchId, "overallCompletion", batch.practicalCompletion, "practicalDetails", practicalData);
    }

    public Map<String, Object> getBatchFeedback(String batchId) {
        List<Map<String, Object>> feedback = feedbackStore.getOrDefault(batchId, Collections.emptyList());
        double avgRating = feedback.stream().mapToDouble(f -> ((Number) f.getOrDefault("rating", 0)).doubleValue()).average().orElse(0.0);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("batchId", batchId);
        summary.put("totalFeedback", feedback.size());
        summary.put("averageRating", Math.round(avgRating * 10.0) / 10.0);
        summary.put("ratingDistribution", Map.of(
            "5", feedback.stream().filter(f -> (int) f.get("rating") == 5).count(),
            "4", feedback.stream().filter(f -> (int) f.get("rating") == 4).count(),
            "3", feedback.stream().filter(f -> (int) f.get("rating") == 3).count(),
            "2", feedback.stream().filter(f -> (int) f.get("rating") == 2).count(),
            "1", feedback.stream().filter(f -> (int) f.get("rating") == 1).count()
        ));
        summary.put("feedbackEntries", feedback);
        return summary;
    }

    public List<Map<String, Object>> getAllBatches() {
        log.info("Fetching all batches");
        List<Map<String, Object>> batches = new ArrayList<>();
        for (Batch batch : batchStore.values()) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("batchId", batch.batchId);
            entry.put("courseName", batch.courseName);
            entry.put("startDate", batch.startDate.toString());
            entry.put("endDate", batch.endDate.toString());
            entry.put("status", batch.status);
            entry.put("totalStudents", batch.totalStudents);
            entry.put("currentWeek", batch.currentWeek);
            entry.put("totalWeeks", batch.totalWeeks);
            entry.put("progressPercentage", Math.round((double) batch.currentWeek / batch.totalWeeks * 100 * 10.0) / 10.0);
            entry.put("trainerName", batch.trainerName);
            batches.add(entry);
        }
        batches.sort((a, b) -> ((String) b.get("startDate")).compareTo((String) a.get("startDate")));
        log.info("Returning {} batches", batches.size());
        return batches;
    }

    // ---- Seed data ----

    private Map<String, Batch> buildBatchStore() {
        Map<String, Batch> store = new LinkedHashMap<>();
        store.put("BATCH-MC-2026-001", new Batch("BATCH-MC-2026-001", "Mushroom Cultivation Mastery",
            LocalDate.of(2026, 1, 15), LocalDate.of(2026, 3, 10), "In Progress",
            "TR-001", "Dr. Sanjay Verma", 7, 8, 82.5, 68.3, 75.0, 20, 25,
            buildBatchStudents("BATCH-MC-2026-001")));
        store.put("BATCH-MC-2026-002", new Batch("BATCH-MC-2026-002", "Mushroom Cultivation Mastery",
            LocalDate.of(2026, 2, 1), LocalDate.of(2026, 3, 28), "In Progress",
            "TR-002", "Prof. Meera Krishnan", 4, 8, 78.0, 62.5, 68.0, 18, 25,
            buildBatchStudents("BATCH-MC-2026-002")));
        store.put("BATCH-MC-2026-003", new Batch("BATCH-MC-2026-003", "Mushroom Cultivation Mastery",
            LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 25), "Upcoming",
            "TR-001", "Dr. Sanjay Verma", 0, 8, 0.0, 0.0, 0.0, 0, 25, List.of()));
        store.put("BATCH-ADV-2026-001", new Batch("BATCH-ADV-2026-001", "Advanced Mushroom Cultivation",
            LocalDate.of(2026, 1, 10), LocalDate.of(2026, 2, 28), "Completed",
            "TR-003", "Dr. Anjali Deshmukh", 8, 8, 91.2, 78.5, 88.0, 15, 20,
            buildBatchStudents("BATCH-ADV-2026-001")));
        store.put("BATCH-SHORT-2026-001", new Batch("BATCH-SHORT-2026-001", "Mushroom Farming: Quick Start",
            LocalDate.of(2026, 3, 15), LocalDate.of(2026, 4, 12), "In Progress",
            "TR-002", "Prof. Meera Krishnan", 2, 4, 85.0, 70.0, 72.0, 22, 30,
            buildBatchStudents("BATCH-SHORT-2026-001")));
        return store;
    }

    private List<BatchStudent> buildBatchStudents(String batchId) {
        String[][] names;
        if ("BATCH-MC-2026-001".equals(batchId)) {
            names = new String[][]{
                {"STU-001", "Aarav Sharma", "aarav.s@email.com", "Active"},
                {"STU-002", "Priya Patel", "priya.p@email.com", "Active"},
                {"STU-003", "Ravi Kumar", "ravi.k@email.com", "Active"},
                {"STU-004", "Sunita Verma", "sunita.v@email.com", "Active"},
                {"STU-005", "Vikram Singh", "vikram.s@email.com", "Active"},
                {"STU-006", "Ananya Gupta", "ananya.g@email.com", "Active"},
                {"STU-007", "Deepak Joshi", "deepak.j@email.com", "Active"}
            };
            names = java.util.Arrays.copyOf(names, 20);
            for (int i = 7; i < 20; i++)
                names[i] = new String[]{"STU-" + String.format("%03d", i + 1), "Student " + (i + 1), "student" + (i + 1) + "@email.com", i % 5 == 0 ? "Inactive" : "Active"};
        } else if ("BATCH-MC-2026-002".equals(batchId)) {
            names = new String[][]{
                {"STU-008", "Kavita Rao", "kavita.r@email.com", "Active"},
                {"STU-009", "Rajesh Nair", "rajesh.n@email.com", "Active"},
                {"STU-010", "Meena Iyer", "meena.i@email.com", "Active"},
                {"STU-011", "Arjun Reddy", "arjun.r@email.com", "Active"},
                {"STU-012", "Lakshmi Menon", "lakshmi.m@email.com", "Active"},
                {"STU-013", "Suresh Desai", "suresh.d@email.com", "Active"},
                {"STU-014", "Pooja Malhotra", "pooja.m@email.com", "Active"}
            };
            names = java.util.Arrays.copyOf(names, 18);
            for (int i = 7; i < 18; i++)
                names[i] = new String[]{"STU-" + String.format("%03d", 14 + i - 6), "Student " + (14 + i - 6), "student" + (14 + i - 6) + "@email.com", "Active"};
        } else if ("BATCH-ADV-2026-001".equals(batchId)) {
            names = new String[][]{
                {"STU-015", "Gaurav Khanna", "gaurav.k@email.com", "Completed"},
                {"STU-016", "Nisha Agarwal", "nisha.a@email.com", "Completed"},
                {"STU-017", "Rohit Choudhary", "rohit.c@email.com", "Completed"},
                {"STU-018", "Sneha Kapoor", "sneha.k@email.com", "Completed"},
                {"STU-019", "Amit Saxena", "amit.s@email.com", "Completed"},
                {"STU-020", "Divya Bhatia", "divya.b@email.com", "Completed"}
            };
            names = java.util.Arrays.copyOf(names, 15);
            for (int i = 6; i < 15; i++)
                names[i] = new String[]{"STU-" + String.format("%03d", 20 + i - 5), "Student " + (20 + i - 5), "student" + (20 + i - 5) + "@email.com", "Completed"};
        } else {
            names = new String[][]{
                {"STU-021", "Rahul Mehta", "rahul.m@email.com", "Active"},
                {"STU-022", "Sonia Kapur", "sonia.k@email.com", "Active"},
                {"STU-023", "Vivek Saxena", "vivek.s@email.com", "Active"},
                {"STU-024", "Neha Jain", "neha.j@email.com", "Active"},
                {"STU-025", "Prakash Rao", "prakash.r@email.com", "Active"}
            };
        }
        List<BatchStudent> students = new ArrayList<>();
        for (String[] s : names) {
            students.add(new BatchStudent(s[0], s[1], s[2],
                Math.round((60 + RANDOM.nextDouble() * 35) * 10.0) / 10.0,
                Math.round((45 + RANDOM.nextDouble() * 45) * 10.0) / 10.0,
                Math.round((50 + RANDOM.nextDouble() * 45) * 10.0) / 10.0,
                s[3], LocalDate.now().minusWeeks(4)));
        }
        return students;
    }

    private Map<String, List<Map<String, Object>>> buildFeedbackStore() {
        Map<String, List<Map<String, Object>>> store = new LinkedHashMap<>();
        store.put("BATCH-MC-2026-001", List.of(
            fb("Course content was excellent and well-structured", 5, "Course Content", "Aarav Sharma"),
            fb("Practical sessions were very hands-on and useful", 5, "Practical Training", "Priya Patel"),
            fb("More time needed for disease identification module", 4, "Pacing", "Ravi Kumar"),
            fb("Trainer was knowledgeable and approachable", 5, "Trainer", "Sunita Verma"),
            fb("Laboratory equipment could be updated", 3, "Facilities", "Vikram Singh"),
            fb("Good balance of theory and practice", 4, "Course Content", "Ananya Gupta")
        ));
        store.put("BATCH-MC-2026-002", List.of(
            fb("Good introduction to mushroom cultivation", 4, "Course Content", "Kavita Rao"),
            fb("Need more practical demonstration videos", 3, "Resources", "Rajesh Nair")
        ));
        store.put("BATCH-ADV-2026-001", List.of(
            fb("Advanced techniques were covered in depth", 5, "Course Content", "Gaurav Khanna"),
            fb("The business planning module was very practical", 5, "Business Module", "Nisha Agarwal"),
            fb("Challenging but rewarding course", 4, "Overall", "Sneha Kapoor"),
            fb("Research paper discussions added great value", 5, "Course Content", "Amit Saxena")
        ));
        return store;
    }

    private Map<String, Object> fb(String comment, int rating, String category, String name) {
        return new LinkedHashMap<>(Map.of("comment", comment, "rating", rating, "category", category, "studentName", name, "date", LocalDate.now().minusDays(RANDOM.nextInt(30)).toString()));
    }

    private String getSessionTopic(Batch batch, int offsetDays) {
        int week = Math.min(batch.currentWeek + (offsetDays / 5), batch.totalWeeks);
        return switch (week) {
            case 1 -> "Mushroom Biology & Lab Safety";
            case 2 -> "Spawn Preparation";
            case 3 -> "Substrate Formulation";
            case 4 -> "Inoculation & Incubation";
            case 5 -> "Fruiting Conditions";
            case 6 -> "Disease Management";
            case 7 -> "Harvesting & Post-Harvest";
            case 8 -> "Business Planning & Review";
            default -> "General Session";
        };
    }

    private String getPracticalName(int number) {
        return switch (number) {
            case 1 -> "Microscope Identification";
            case 2 -> "Sterile Technique";
            case 3 -> "Substrate Preparation";
            case 4 -> "Spawn Inoculation";
            case 5 -> "Fruiting Chamber Setup";
            case 6 -> "Disease Diagnosis";
            case 7 -> "Harvest & Packaging";
            case 8 -> "Final Project Presentation";
            default -> "Practical " + number;
        };
    }

    public record BatchStudent(String studentId, String studentName, String email, double attendance, double assessmentScore, double practicalProgress, String status, LocalDate enrolledDate) {}
    public record Batch(String batchId, String courseName, LocalDate startDate, LocalDate endDate, String status, String trainerId, String trainerName, int currentWeek, int totalWeeks, double avgAttendance, double avgAssessmentScore, double practicalCompletion, int totalStudents, int maxCapacity, List<BatchStudent> students) {}
}
