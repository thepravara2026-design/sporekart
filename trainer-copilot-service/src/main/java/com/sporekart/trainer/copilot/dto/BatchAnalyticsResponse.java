package com.sporekart.trainer.copilot.dto;

import java.util.List;
import java.util.Map;

import com.sporekart.trainer.copilot.domain.StudentProgress;

public record BatchAnalyticsResponse(
    String batchId,
    String batchName,
    String courseName,
    String status,
    int capacity,
    int enrolledCount,
    double avgAttendance,
    double avgScore,
    int completedModules,
    int totalModules,
    Map<String, Double> moduleCompletionRates,
    List<StudentProgress> topStudents,
    List<StudentProgress> atRiskStudents
) {}
