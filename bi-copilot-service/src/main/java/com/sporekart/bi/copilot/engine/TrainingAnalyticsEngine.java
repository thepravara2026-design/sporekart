package com.sporekart.bi.copilot.engine;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sporekart.bi.copilot.domain.TrainingAnalytics;

@Component
public class TrainingAnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(TrainingAnalyticsEngine.class);
    private static final Random RANDOM = new Random(202);
    private static final DateTimeFormatter PERIOD_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final List<String> COURSES = List.of(
        "Mushroom Cultivation Fundamentals",
        "Advanced Spawn Production",
        "Commercial Mushroom Farming"
    );

    private static final List<String> MODULES = List.of(
        "Sterilization Techniques",
        "Substrate Preparation",
        "Inoculation Methods",
        "Environmental Control",
        "Harvesting & Storage",
        "Quality Assurance",
        "Business Planning",
        "Marketing & Sales"
    );

    private static final double[] MONTHLY_STUDENTS = {45, 52, 48, 60, 58, 72};
    private static final double[] MONTHLY_BATCHES = {3, 4, 3, 4, 4, 5};
    private static final double[] MONTHLY_COMPLETION = {0.78, 0.80, 0.82, 0.84, 0.85, 0.87};
    private static final double[] MONTHLY_REVENUE = {180000, 210000, 195000, 250000, 240000, 310000};
    private static final double[] MONTHLY_COST = {95000, 110000, 102000, 130000, 125000, 160000};

    private final List<SeedTrainingMonth> seedData = generateSeedData();

    public TrainingAnalyticsEngine() {
        log.info("TrainingAnalyticsEngine initialized with {} months of seed data", seedData.size());
    }

    public TrainingAnalytics getTrainingSummary(String period) {
        SeedTrainingMonth sm = resolvePeriod(period);
        if (sm == null) return emptyAnalytics(period);

        return new TrainingAnalytics(
            UUID.randomUUID().toString(),
            period,
            sm.totalStudents,
            sm.totalBatches,
            sm.activeBatches,
            sm.completedBatches,
            84.0 + RANDOM.nextDouble() * 10,
            sm.averageScore,
            sm.certificationsIssued,
            sm.pendingCertifications,
            sm.completionRate * 100,
            sm.scoreByModule,
            sm.studentsByCourse,
            sm.revenue,
            sm.cost,
            sm.cost > 0 ? (sm.revenue - sm.cost) / sm.revenue * 100 : 0
        );
    }

    public Map<String, Object> getStudentPerformanceByCourse() {
        Map<String, Object> result = new LinkedHashMap<>();

        for (String course : COURSES) {
            Map<String, Object> perf = new LinkedHashMap<>();
            int students = 50 + RANDOM.nextInt(80);
            double avgScore = 65 + RANDOM.nextDouble() * 30;
            double passRate = 0.75 + RANDOM.nextDouble() * 0.2;
            int certified = (int) (students * passRate);

            perf.put("totalStudents", students);
            perf.put("averageScore", Math.round(avgScore * 100) / 100.0);
            perf.put("passRate", Math.round(passRate * 10000) / 100.0);
            perf.put("certifiedStudents", certified);
            perf.put("topScore", Math.round((avgScore + 15 + RANDOM.nextDouble() * 10) * 100) / 100.0);
            perf.put("lowestScore", Math.round((avgScore - 20 + RANDOM.nextDouble() * 10) * 100) / 100.0);

            result.put(course, perf);
        }
        return result;
    }

    public double getCertificationRate() {
        SeedTrainingMonth latest = seedData.getLast();
        return latest.totalStudents > 0
            ? (double) latest.certificationsIssued / latest.totalStudents * 100
            : 0;
    }

    public double getTrainingRevenue() {
        return seedData.stream().mapToDouble(s -> s.revenue).sum();
    }

    public double getTrainingProfitMargin() {
        double totalRevenue = seedData.stream().mapToDouble(s -> s.revenue).sum();
        double totalCost = seedData.stream().mapToDouble(s -> s.cost).sum();
        return totalRevenue > 0 ? (totalRevenue - totalCost) / totalRevenue * 100 : 0;
    }

    public Map<String, Double> getScoreDistributionByModule() {
        Map<String, Double> distribution = new LinkedHashMap<>();
        for (String module : MODULES) {
            distribution.put(module, Math.round((60 + RANDOM.nextDouble() * 35) * 100) / 100.0);
        }
        return distribution;
    }

    public double getStudentRetention() {
        return 0.82 + RANDOM.nextDouble() * 0.12;
    }

    public List<Map<String, Object>> getTopPerformingCourses() {
        List<Map<String, Object>> topCourses = new ArrayList<>();

        for (int i = 0; i < COURSES.size(); i++) {
            String course = COURSES.get(i);
            double avgScore = 75 + RANDOM.nextDouble() * 20 - (i * 3);
            int enrollments = 55 + RANDOM.nextInt(60) - (i * 8);

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("courseName", course);
            entry.put("averageScore", Math.round(avgScore * 100) / 100.0);
            entry.put("enrollments", enrollments);
            entry.put("completionRate", Math.round((0.78 + RANDOM.nextDouble() * 0.18) * 10000) / 100.0);
            entry.put("certificationRate", Math.round((0.70 + RANDOM.nextDouble() * 0.22) * 10000) / 100.0);

            topCourses.add(entry);
        }

        topCourses.sort((a, b) -> Double.compare(
            (Double) b.get("averageScore"),
            (Double) a.get("averageScore")
        ));
        return topCourses;
    }

    private SeedTrainingMonth resolvePeriod(String period) {
        if (period == null || period.isBlank()) return seedData.getLast();
        for (SeedTrainingMonth sm : seedData) {
            if (sm.periodLabel.equals(period)) return sm;
        }
        return null;
    }

    private TrainingAnalytics emptyAnalytics(String period) {
        return new TrainingAnalytics(
            UUID.randomUUID().toString(), period, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            Map.of(), Map.of(), 0, 0, 0
        );
    }

    private List<SeedTrainingMonth> generateSeedData() {
        List<SeedTrainingMonth> data = new ArrayList<>();
        LocalDate base = LocalDate.of(2025, 7, 1);

        for (int i = 0; i < 6; i++) {
            LocalDate monthStart = base.plusMonths(i);
            String label = monthStart.format(PERIOD_FMT);
            int totalStudents = (int) (MONTHLY_STUDENTS[i] + RANDOM.nextInt(10) - 3);
            int totalBatches = (int) (MONTHLY_BATCHES[i] + RANDOM.nextInt(2) - 0);
            int activeBatches = Math.max(1, totalBatches - RANDOM.nextInt(2));
            int completedBatches = totalBatches - activeBatches;
            double completionRate = MONTHLY_COMPLETION[i] + RANDOM.nextDouble() * 0.03 - 0.015;
            double avgScore = 68 + RANDOM.nextDouble() * 12;
            int certified = (int) (totalStudents * completionRate * (0.85 + RANDOM.nextDouble() * 0.1));
            int pending = (int) (totalStudents * (1 - completionRate) * 0.5);

            Map<String, Double> scoreByModule = new LinkedHashMap<>();
            for (String module : MODULES) {
                scoreByModule.put(module, Math.round((60 + RANDOM.nextDouble() * 35) * 100) / 100.0);
            }

            Map<String, Integer> studentsByCourse = new LinkedHashMap<>();
            int remaining = totalStudents;
            for (int c = 0; c < COURSES.size(); c++) {
                int count = c < COURSES.size() - 1
                    ? (int) (totalStudents * (0.25 + RANDOM.nextDouble() * 0.15))
                    : remaining;
                studentsByCourse.put(COURSES.get(c), count);
                remaining -= count;
            }

            double revenue = MONTHLY_REVENUE[i] + RANDOM.nextDouble() * 30000 - 15000;
            double cost = MONTHLY_COST[i] + RANDOM.nextDouble() * 15000 - 7500;

            data.add(new SeedTrainingMonth(
                i, label, totalStudents, totalBatches, activeBatches, completedBatches,
                completionRate, avgScore, certified, pending,
                scoreByModule, studentsByCourse, revenue, cost
            ));
        }
        return data;
    }

    private record SeedTrainingMonth(
        int monthIndex,
        String periodLabel,
        int totalStudents,
        int totalBatches,
        int activeBatches,
        int completedBatches,
        double completionRate,
        double averageScore,
        int certificationsIssued,
        int pendingCertifications,
        Map<String, Double> scoreByModule,
        Map<String, Integer> studentsByCourse,
        double revenue,
        double cost
    ) {}
}
