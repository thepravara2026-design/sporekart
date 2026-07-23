package com.sporekart.bi.copilot.domain;

import java.util.Map;

public record TrainingAnalytics(
    String analyticsId,
    String period,
    int totalStudents,
    int totalBatches,
    int activeBatches,
    int completedBatches,
    double averageAttendance,
    double averageScore,
    int totalCertificationsIssued,
    int pendingCertifications,
    double completionRate,
    Map<String, Double> scoreByModule,
    Map<String, Integer> studentsByCourse,
    double revenueFromTraining,
    double trainingCost,
    double trainingProfitMargin
) {}
