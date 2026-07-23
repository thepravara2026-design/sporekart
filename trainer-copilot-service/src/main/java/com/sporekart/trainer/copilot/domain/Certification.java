package com.sporekart.trainer.copilot.domain;

import java.util.List;

public record Certification(
    String certificationId,
    String studentId,
    String studentName,
    String batchId,
    String courseName,
    double attendanceScore,
    double assessmentScore,
    double practicalScore,
    double overallScore,
    String recommendation,
    String status,
    List<String> remarks,
    String issuedDate,
    String expiryDate
) {}
