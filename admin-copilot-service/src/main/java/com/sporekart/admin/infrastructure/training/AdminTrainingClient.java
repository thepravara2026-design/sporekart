package com.sporekart.admin.infrastructure.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AdminTrainingClient {

    private static final Logger log = LoggerFactory.getLogger(AdminTrainingClient.class);
    private static final Random RAND = new Random(42);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Map<String, Object> getTrainingMetrics(String period) {
        log.info("Fetching training metrics for period: {}", period);
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("period", period);
        metrics.put("totalCourses", 10);
        metrics.put("activeEnrollments", 150 + RAND.nextInt(200));
        metrics.put("totalCompletions", 80 + RAND.nextInt(100));
        metrics.put("completionRate", Math.round((45.0 + RAND.nextDouble() * 30.0) * 10.0) / 10.0);
        metrics.put("averageRating", Math.round((3.8 + RAND.nextDouble() * 1.0) * 10.0) / 10.0);

        List<Map<String, Object>> courseMetrics = new ArrayList<>();
        String[][] courses = {
                {"Mushroom Farming 101", "Beginner", "234", "178", "4.5"},
                {"Advanced Spawn Production", "Advanced", "89", "52", "4.2"},
                {"Organic Composting Masterclass", "Intermediate", "156", "98", "4.3"},
                {"Mushroom Business Essentials", "Business", "167", "112", "4.1"},
                {"Hydroponic Mushroom Cultivation", "Advanced", "45", "22", "3.9"},
                {"Quality Control in Mushroom Farming", "Intermediate", "78", "45", "4.4"},
                {"Mushroom Processing & Preservation", "Intermediate", "123", "76", "4.2"},
                {"Marketing Your Mushroom Brand", "Business", "89", "54", "4.0"},
                {"Sustainable Farming Practices", "Beginner", "198", "134", "4.6"},
                {"Mushroom Export Documentation", "Business", "56", "28", "3.8"}
        };
        for (String[] c : courses) {
            Map<String, Object> cm = new LinkedHashMap<>();
            cm.put("title", c[0]);
            cm.put("level", c[1]);
            cm.put("enrolled", Integer.parseInt(c[2]));
            cm.put("completed", Integer.parseInt(c[3]));
            cm.put("rating", Double.parseDouble(c[4]));
            cm.put("completionRate", Math.round(
                    Double.parseDouble(c[3]) / Double.parseDouble(c[2]) * 1000.0) / 10.0);
            courseMetrics.add(cm);
        }
        metrics.put("courseMetrics", courseMetrics);

        Map<String, Object> trends = new LinkedHashMap<>();
        trends.put("weeklyEnrollmentGrowth", Math.round((5.0 + RAND.nextDouble() * 15.0) * 10.0) / 10.0);
        trends.put("monthlyActiveUsers", 180 + RAND.nextInt(150));
        trends.put("averageSessionDurationMin", 22 + RAND.nextInt(18));
        trends.put("certificatesIssued", 30 + RAND.nextInt(60));
        metrics.put("trends", trends);

        return metrics;
    }

    public Map<String, Object> getBatchStatus() {
        log.info("Fetching training batch status");
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("totalBatches", 6);
        status.put("activeBatches", 4);
        status.put("completedBatches", 2);
        status.put("upcomingBatches", 2);

        List<Map<String, Object>> batches = new ArrayList<>();
        String[][] batchData = {
                {"BATCH-001", "Mushroom Farming 101", "Active", "2026-07-01", "2026-08-15", "45"},
                {"BATCH-002", "Advanced Spawn Production", "Active", "2026-07-10", "2026-08-30", "22"},
                {"BATCH-003", "Organic Composting Masterclass", "Active", "2026-07-15", "2026-09-01", "35"},
                {"BATCH-004", "Mushroom Business Essentials", "Active", "2026-07-20", "2026-09-05", "28"},
                {"BATCH-005", "Sustainable Farming Practices", "Completed", "2026-05-01", "2026-06-15", "50"},
                {"BATCH-006", "Mushroom Processing & Preservation", "Completed", "2026-05-15", "2026-06-30", "32"},
                {"BATCH-007", "Hydroponic Mushroom Cultivation", "Upcoming", "2026-08-01", "2026-09-15", "0"},
                {"BATCH-008", "Marketing Your Mushroom Brand", "Upcoming", "2026-08-10", "2026-09-25", "0"}
        };
        for (String[] b : batchData) {
            Map<String, Object> batch = new LinkedHashMap<>();
            batch.put("batchId", b[0]);
            batch.put("courseName", b[1]);
            batch.put("status", b[2]);
            batch.put("startDate", b[3]);
            batch.put("endDate", b[4]);
            batch.put("enrolledCount", Integer.parseInt(b[5]));
            batches.add(batch);
        }
        status.put("batches", batches);
        return status;
    }

    public Map<String, Object> getEnrollmentTrends() {
        log.info("Fetching enrollment trends");
        Map<String, Object> trends = new LinkedHashMap<>();
        LocalDate today = LocalDate.of(2026, 7, 23);

        List<Map<String, Object>> monthlyEnrollments = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate month = today.minusMonths(i);
            Map<String, Object> me = new LinkedHashMap<>();
            me.put("month", month.getMonth().toString().substring(0, 3) + " " + month.getYear());
            me.put("newEnrollments", 40 + RAND.nextInt(100));
            me.put("completions", 25 + RAND.nextInt(60));
            me.put("activeLearners", 120 + RAND.nextInt(100));
            monthlyEnrollments.add(me);
        }
        trends.put("monthlyTrend", monthlyEnrollments);

        Map<String, Object> byLevel = new LinkedHashMap<>();
        byLevel.put("Beginner", 432);
        byLevel.put("Intermediate", 357);
        byLevel.put("Advanced", 134);
        byLevel.put("Business", 312);
        trends.put("enrollmentsByLevel", byLevel);

        Map<String, Object> byDevice = new LinkedHashMap<>();
        byDevice.put("Mobile", 58.0);
        byDevice.put("Desktop", 32.0);
        byDevice.put("Tablet", 10.0);
        trends.put("enrollmentsByDevice", byDevice);

        trends.put("peakEnrollmentDay", "Monday");
        trends.put("averageTimeToEnroll", "2.5 days from signup");
        trends.put("mostPopularCourse", "Mushroom Farming 101");

        return trends;
    }
}
