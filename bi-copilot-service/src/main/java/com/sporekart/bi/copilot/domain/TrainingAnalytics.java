package com.sporekart.bi.copilot.domain;

import java.util.List;
import java.util.Map;

public record TrainingAnalytics(
    String period,
    int totalBatches,
    int activeBatches,
    int completedBatches,
    int totalStudents,
    double averageAttendance,
    double averageScore,
    double completionRate,
    int certificationsIssued,
    List<TrainerPerformance> trainerPerformance,
    Map<String, Double> revenueByTraining,
    Map<String, Integer> enrollmentByCourse
) {
    public record TrainerPerformance(String trainerId, String name, int batches, int students, double avgScore, double completionRate) {}
}
