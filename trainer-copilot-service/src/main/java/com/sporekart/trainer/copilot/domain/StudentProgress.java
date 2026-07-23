package com.sporekart.trainer.copilot.domain;

import java.util.List;
import java.util.Map;

public record StudentProgress(
    String studentId,
    String studentName,
    String batchId,
    double attendancePercent,
    Map<String, Double> moduleScores,
    Map<String, String> moduleStatus,
    double overallScore,
    int completedAssignments,
    int totalAssignments,
    int completedPracticals,
    int totalPracticals,
    String certificationStatus,
    List<String> weakTopics,
    List<String> strongTopics,
    String lastUpdated
) {}
