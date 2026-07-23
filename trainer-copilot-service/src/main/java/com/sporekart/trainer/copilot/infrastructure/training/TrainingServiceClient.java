package com.sporekart.trainer.copilot.infrastructure.training;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TrainingServiceClient {

    private final Map<String, BatchData> batches = new ConcurrentHashMap<>();
    private final Map<String, List<StudentData>> studentsByBatch = new ConcurrentHashMap<>();
    private final Map<String, List<AttendanceRecord>> attendanceByBatch = new ConcurrentHashMap<>();
    private final Map<String, List<ScoreRecord>> scoresByBatch = new ConcurrentHashMap<>();

    public record BatchData(
        String batchId,
        String batchName,
        String courseName,
        String startDate,
        String endDate,
        String status,
        int capacity,
        int enrolledCount,
        String trainerName,
        String location
    ) {}

    public record StudentData(
        String studentId,
        String studentName,
        String email,
        String phone,
        String enrollmentDate,
        String status
    ) {}

    public record AttendanceRecord(
        String studentId,
        String studentName,
        String date,
        String status,
        String remarks
    ) {}

    public record ScoreRecord(
        String studentId,
        String studentName,
        String assessmentId,
        String assessmentName,
        double score,
        double maxScore,
        String gradedDate
    ) {}

    @PostConstruct
    public void init() {
        seedBatches();
    }

    public List<BatchData> getBatches() {
        return new ArrayList<>(batches.values());
    }

    public BatchData getBatchDetails(String batchId) {
        return batches.get(batchId);
    }

    public List<StudentData> getStudents(String batchId) {
        return studentsByBatch.getOrDefault(batchId, List.of());
    }

    public List<AttendanceRecord> getAttendance(String batchId) {
        return attendanceByBatch.getOrDefault(batchId, List.of());
    }

    public List<ScoreRecord> getScores(String batchId) {
        return scoresByBatch.getOrDefault(batchId, List.of());
    }

    private void seedBatches() {
        // Batch 1: Mushroom Cultivation 101
        String batch1Id = "BATCH-MC-101";
        batches.put(batch1Id, new BatchData(
            batch1Id, "Mushroom Cultivation 101",
            "Fundamentals of Mushroom Farming",
            "2026-01-15", "2026-03-15",
            "ACTIVE", 30, 25,
            "Dr. Aruna Sharma", "Pune, Maharashtra"
        ));
        seedStudentsForBatch(batch1Id, 25, "MC");
        seedAttendanceForBatch(batch1Id, "MC", 20);
        seedScoresForBatch(batch1Id, "MC", 3);

        // Batch 2: Advanced Oyster Mushroom
        String batch2Id = "BATCH-AOM-202";
        batches.put(batch2Id, new BatchData(
            batch2Id, "Advanced Oyster Mushroom Cultivation",
            "Specialized Techniques for Pleurotus Species",
            "2026-02-01", "2026-04-01",
            "ACTIVE", 20, 18,
            "Prof. Vikram Patil", "Bangalore, Karnataka"
        ));
        seedStudentsForBatch(batch2Id, 18, "AOM");
        seedAttendanceForBatch(batch2Id, "AOM", 15);
        seedScoresForBatch(batch2Id, "AOM", 4);

        // Batch 3: Commercial Mushroom Farming
        String batch3Id = "BATCH-CMF-303";
        batches.put(batch3Id, new BatchData(
            batch3Id, "Commercial Mushroom Farming",
            "Business and Large-Scale Production Management",
            "2026-03-01", "2026-05-30",
            "UPCOMING", 20, 15,
            "Dr. Suresh Deshmukh", "Delhi, NCR"
        ));
        seedStudentsForBatch(batch3Id, 15, "CMF");
        seedAttendanceForBatch(batch3Id, "CMF", 10);
        seedScoresForBatch(batch3Id, "CMF", 2);
    }

    private void seedStudentsForBatch(String batchId, int count, String prefix) {
        List<StudentData> students = new ArrayList<>();
        String[] firstNames = {"Amit", "Priya", "Rahul", "Sneha", "Vikram", "Anita", "Rajesh", "Kavita", "Sunil", "Deepa",
            "Manish", "Neha", "Sanjay", "Pooja", "Arun", "Meena", "Vijay", "Shweta", "Ravi", "Anjali",
            "Sachin", "Ritu", "Gaurav", "Nisha", "Akash"};
        for (int i = 1; i <= count; i++) {
            String sid = String.format("STD-%s-%03d", prefix, i);
            students.add(new StudentData(
                sid,
                firstNames[(i - 1) % firstNames.length] + " " + (char)('A' + (i - 1) / firstNames.length) + ".",
                sid.toLowerCase() + "@sporekart.com",
                "+91-98765" + String.format("%05d", i),
                "2026-01-10",
                "ACTIVE"
            ));
        }
        studentsByBatch.put(batchId, students);
    }

    private void seedAttendanceForBatch(String batchId, String prefix, int sessions) {
        List<AttendanceRecord> records = new ArrayList<>();
        List<StudentData> students = studentsByBatch.get(batchId);
        if (students == null) return;
        LocalDate baseDate = LocalDate.of(2026, 1, 20);
        String[] statuses = {"PRESENT", "PRESENT", "PRESENT", "ABSENT", "PRESENT", "LATE", "PRESENT", "PRESENT", "ABSENT", "PRESENT"};
        for (int s = 0; s < sessions; s++) {
            String date = baseDate.plusDays(s * 3).toString();
            int finalS = s;
            for (StudentData student : students) {
                String status = statuses[(s + student.studentId().hashCode()) % statuses.length];
                String remarks = "ABSENT".equals(status) ? "No prior intimation" :
                                 "LATE".equals(status) ? "Arrived 15 min late" : "";
                records.add(new AttendanceRecord(
                    student.studentId(),
                    student.studentName(),
                    date,
                    status,
                    remarks
                ));
            }
        }
        attendanceByBatch.put(batchId, records);
    }

    private void seedScoresForBatch(String batchId, String prefix, int assessments) {
        List<ScoreRecord> records = new ArrayList<>();
        List<StudentData> students = studentsByBatch.get(batchId);
        if (students == null) return;
        String[] assessmentNames = {
            "Module 1 - Introduction to Mushroom Cultivation",
            "Module 2 - Substrate Preparation and Sterilization",
            "Module 3 - Spawn Run and Fruiting Management",
            "Module 4 - Harvesting and Post-Harvest Technology"
        };
        for (int a = 0; a < assessments && a < assessmentNames.length; a++) {
            String assId = String.format("ASSESS-%s-%d", prefix, a + 1);
            double maxScore = 100.0;
            for (StudentData student : students) {
                double score = 40 + (Math.abs(student.studentId().hashCode() * (a + 1)) % 60) + (a * 2);
                if (score > maxScore) score = maxScore;
                records.add(new ScoreRecord(
                    student.studentId(),
                    student.studentName(),
                    assId,
                    assessmentNames[a],
                    score,
                    maxScore,
                    LocalDate.of(2026, 2, 10 + a * 14).toString()
                ));
            }
        }
        scoresByBatch.put(batchId, records);
    }
}
