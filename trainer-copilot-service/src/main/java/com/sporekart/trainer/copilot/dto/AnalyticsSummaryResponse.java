package com.sporekart.trainer.copilot.dto;

import java.util.Map;

public record AnalyticsSummaryResponse(
    int totalStudents,
    int totalBatches,
    int activeBatches,
    int completedBatches,
    double avgAttendanceAcrossBatches,
    double avgScoreAcrossBatches,
    int totalCertificationsIssued,
    int pendingCertifications,
    int atRiskStudents,
    Map<String, Object> trends
) {}
