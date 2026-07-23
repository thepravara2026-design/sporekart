package com.sporekart.admin.domain;

import java.math.BigDecimal;
import java.util.List;

public record TrainingAnalytics(
    int totalCourses,
    int activeBatches,
    int totalEnrollments,
    BigDecimal completionRate,
    List<String> upcomingBatches,
    BigDecimal revenueFromTraining,
    List<String> popularCourses
) {}
