package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.TrainingAnalytics;
import com.sporekart.bi.copilot.domain.TrainingAnalytics.TrainerPerformance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TrainingAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(TrainingAnalyticsEngine.class);

    private static final String[] COURSES = {
        "Mushroom Cultivation 101", "Advanced Oyster Farming",
        "Commercial Farming Program", "Disease Management Course",
        "Spawn Production Workshop"
    };
    private static final double[] COURSE_PRICES = {5000, 12000, 25000, 8000, 15000};
    private static final int[] COURSE_DURATIONS = {5, 10, 20, 7, 12};

    private static final String[] TRAINERS = {
        "Dr. Anil Deshmukh", "Prof. Sunita Patil", "Mr. Rajendra Kulkarni",
        "Dr. Meena Joshi", "Mr. Prakash Hegde", "Ms. Swati Naik",
        "Dr. Kiran Thakur", "Mr. Mahesh Wagh", "Ms. Pooja Rane", "Dr. Sameer Khanna"
    };

    private final List<Batch> batches = new ArrayList<>();
    private final List<Trainer> trainers = new ArrayList<>();
    private static final int TOTAL_STUDENTS = 500;
    private static final int BATCHES_PER_MONTH = 8;

    public TrainingAnalyticsEngine() {
        generateSeedData();
    }

    record Trainer(String id, String name, String specialization, double rating) {}

    record Batch(
        String batchId, String course, String trainerId, YearMonth startMonth,
        int capacity, int enrolled, int attended, int completed,
        int certified, double avgScore
    ) {}

    private void generateSeedData() {
        var rand = new Random(42);

        for (int i = 0; i < TRAINERS.length; i++) {
            String id = "TR" + String.format("%02d", i + 1);
            String spec = COURSES[i % COURSES.length];
            double rating = Math.round((3.5 + rand.nextDouble() * 1.5) * 100.0) / 100.0;
            trainers.add(new Trainer(id, TRAINERS[i], spec, rating));
        }

        YearMonth start = YearMonth.now().minusMonths(11);
        int batchCounter = 0;

        for (int i = 0; i < 12; i++) {
            YearMonth ym = start.plusMonths(i);
            for (int b = 0; b < BATCHES_PER_MONTH; b++) {
                String batchId = "BATCH" + String.format("%04d", ++batchCounter);
                String course = COURSES[rand.nextInt(COURSES.length)];
                String trainerId = trainers.get(rand.nextInt(trainers.size())).id();

                int capacity = switch (course) {
                    case "Mushroom Cultivation 101" -> 30;
                    case "Advanced Oyster Farming" -> 25;
                    case "Commercial Farming Program" -> 20;
                    case "Disease Management Course" -> 25;
                    case "Spawn Production Workshop" -> 20;
                    default -> 25;
                };

                double enrollmentRate = 0.5 + rand.nextDouble() * 0.5;
                int enrolled = Math.min(capacity, Math.max(5, (int) (capacity * enrollmentRate)));
                int attended = (int) (enrolled * (0.7 + rand.nextDouble() * 0.25));
                int completed = (int) (attended * (0.65 + rand.nextDouble() * 0.3));
                int certified = (int) (completed * (0.6 + rand.nextDouble() * 0.35));
                double avgScore = Math.round((50 + rand.nextDouble() * 45) * 100.0) / 100.0;

                batches.add(new Batch(batchId, course, trainerId, ym, capacity, enrolled, attended, completed, certified, avgScore));
            }
        }
        log.info("Generated {} training batches across {} months with {} trainers", batches.size(), 12, trainers.size());
    }

    private YearMonth resolveYearMonth(String period) {
        if (period == null || "current".equalsIgnoreCase(period)) return YearMonth.now();
        try { return YearMonth.parse(period, DateTimeFormatter.ofPattern("yyyy-MM")); }
        catch (Exception e) { return YearMonth.now(); }
    }

    private List<Batch> filterByPeriod(String period) {
        YearMonth ym = resolveYearMonth(period);
        return batches.stream().filter(b -> b.startMonth().equals(ym)).toList();
    }

    public TrainingAnalytics getTrainingSummary(String period) {
        var periodBatches = filterByPeriod(period);
        var allBatches = batches;

        int totalBatches = periodBatches.size();
        int activeBatches = (int) periodBatches.stream().filter(b -> b.completed() < b.enrolled()).count();
        int completedBatches = (int) periodBatches.stream().filter(b -> b.completed() == b.enrolled()).count();
        int totalStudents = periodBatches.stream().mapToInt(Batch::enrolled).sum();
        double avgAttendance = periodBatches.stream()
            .mapToDouble(b -> b.enrolled() > 0 ? (double) b.attended() / b.enrolled() * 100 : 0)
            .average().orElse(0);
        avgAttendance = Math.round(avgAttendance * 100.0) / 100.0;
        double avgScore = periodBatches.stream().mapToDouble(Batch::avgScore).average().orElse(0);
        avgScore = Math.round(avgScore * 100.0) / 100.0;
        double completionRate = periodBatches.stream()
            .mapToDouble(b -> b.attended() > 0 ? (double) b.completed() / b.attended() * 100 : 0)
            .average().orElse(0);
        completionRate = Math.round(completionRate * 100.0) / 100.0;
        int certifications = periodBatches.stream().mapToInt(Batch::certified).sum();

        var trainerPerf = getTrainerPerformance();
        var revenueByTraining = getRevenueByTraining(period);
        var enrollmentByCourse = getEnrollmentByCourse(period);

        return new TrainingAnalytics(
            period, totalBatches, activeBatches, completedBatches, totalStudents,
            avgAttendance, avgScore, completionRate, certifications,
            trainerPerf, revenueByTraining, enrollmentByCourse
        );
    }

    public Map<String, Object> getBatchPerformance(String batchId) {
        var batch = batches.stream().filter(b -> b.batchId().equalsIgnoreCase(batchId))
            .findFirst().orElse(null);
        if (batch == null) return Map.of("error", "Batch not found");

        var trainer = trainers.stream().filter(t -> t.id().equals(batch.trainerId())).findFirst().orElse(null);

        var map = new LinkedHashMap<String, Object>();
        map.put("batchId", batch.batchId());
        map.put("course", batch.course());
        map.put("trainer", trainer != null ? trainer.name() : "Unknown");
        map.put("startMonth", batch.startMonth().toString());
        map.put("capacity", batch.capacity());
        map.put("enrolled", batch.enrolled());
        map.put("attended", batch.attended());
        map.put("completed", batch.completed());
        map.put("certified", batch.certified());
        map.put("avgScore", batch.avgScore());
        map.put("attendanceRate", batch.enrolled() > 0
            ? Math.round((double) batch.attended() / batch.enrolled() * 10000.0) / 100.0 : 0);
        map.put("completionRate", batch.attended() > 0
            ? Math.round((double) batch.completed() / batch.attended() * 10000.0) / 100.0 : 0);
        map.put("certificationRate", batch.completed() > 0
            ? Math.round((double) batch.certified() / batch.completed() * 10000.0) / 100.0 : 0);
        return map;
    }

    public Map<String, Integer> getEnrollmentByCourse(String period) {
        var periodBatches = filterByPeriod(period);
        var map = new LinkedHashMap<String, Integer>();
        for (String course : COURSES) {
            int total = periodBatches.stream()
                .filter(b -> b.course().equals(course))
                .mapToInt(Batch::enrolled).sum();
            map.put(course, total);
        }
        return map;
    }

    public double getAttendanceRate(String period) {
        var periodBatches = filterByPeriod(period);
        if (periodBatches.isEmpty()) return 0;
        return Math.round(periodBatches.stream()
            .mapToDouble(b -> b.enrolled() > 0 ? (double) b.attended() / b.enrolled() * 100 : 0)
            .average().orElse(0) * 100.0) / 100.0;
    }

    public double getCompletionRate(String period) {
        var periodBatches = filterByPeriod(period);
        if (periodBatches.isEmpty()) return 0;
        return Math.round(periodBatches.stream()
            .mapToDouble(b -> b.attended() > 0 ? (double) b.completed() / b.attended() * 100 : 0)
            .average().orElse(0) * 100.0) / 100.0;
    }

    public double getCertificationRate(String period) {
        var periodBatches = filterByPeriod(period);
        if (periodBatches.isEmpty()) return 0;
        return Math.round(periodBatches.stream()
            .mapToDouble(b -> b.completed() > 0 ? (double) b.certified() / b.completed() * 100 : 0)
            .average().orElse(0) * 100.0) / 100.0;
    }

    public List<TrainerPerformance> getTrainerPerformance() {
        return trainers.stream().map(t -> {
            var tBatches = batches.stream().filter(b -> b.trainerId().equals(t.id())).toList();
            int batchCount = tBatches.size();
            int studentCount = tBatches.stream().mapToInt(Batch::enrolled).sum();
            double avgScore = tBatches.stream().mapToDouble(Batch::avgScore).average().orElse(0);
            double compRate = tBatches.stream()
                .filter(b -> b.attended() > 0)
                .mapToDouble(b -> (double) b.completed() / b.attended() * 100)
                .average().orElse(0);
            return new TrainerPerformance(t.id(), t.name(), batchCount, studentCount,
                Math.round(avgScore * 100.0) / 100.0, Math.round(compRate * 100.0) / 100.0);
        }).toList();
    }

    public Map<String, Double> getRevenueByTraining(String period) {
        var periodBatches = filterByPeriod(period);
        var map = new LinkedHashMap<String, Double>();
        for (int i = 0; i < COURSES.length; i++) {
            String course = COURSES[i];
            int enrolled = periodBatches.stream()
                .filter(b -> b.course().equals(course))
                .mapToInt(Batch::enrolled).sum();
            map.put(course, Math.round(enrolled * COURSE_PRICES[i] * 100.0) / 100.0);
        }
        return map;
    }

    public Map<String, Object> getStudentProgress(String batchId) {
        var batch = batches.stream().filter(b -> b.batchId().equalsIgnoreCase(batchId))
            .findFirst().orElse(null);
        if (batch == null) return Map.of("error", "Batch not found");

        var map = new LinkedHashMap<String, Object>();
        map.put("batchId", batch.batchId());
        map.put("course", batch.course());
        map.put("totalStudents", batch.enrolled());
        map.put("attended", batch.attended());
        map.put("completed", batch.completed());
        map.put("certified", batch.certified());
        map.put("droppedOut", batch.enrolled() - batch.completed());
        map.put("avgScore", batch.avgScore());

        var stages = new ArrayList<Map<String, Object>>();
        stages.add(Map.of("stage", "Enrolled", "count", batch.enrolled(), "pct", 100.0));
        stages.add(Map.of("stage", "Attended", "count", batch.attended(),
            "pct", Math.round((double) batch.attended() / batch.enrolled() * 10000.0) / 100.0));
        stages.add(Map.of("stage", "Completed", "count", batch.completed(),
            "pct", Math.round((double) batch.completed() / batch.enrolled() * 10000.0) / 100.0));
        stages.add(Map.of("stage", "Certified", "count", batch.certified(),
            "pct", Math.round((double) batch.certified() / batch.enrolled() * 10000.0) / 100.0));
        map.put("funnel", stages);

        return map;
    }

    public List<TrainerPerformance> getTopTrainers(int limit) {
        return getTrainerPerformance().stream()
            .sorted((a, b) -> Double.compare(b.avgScore(), a.avgScore()))
            .limit(limit)
            .toList();
    }
}
