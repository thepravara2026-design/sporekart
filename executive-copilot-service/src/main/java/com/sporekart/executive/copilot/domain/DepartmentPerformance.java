package com.sporekart.executive.copilot.domain;

import java.util.List;
import java.util.Map;

public record DepartmentPerformance(
    String department,
    double overallScore,
    double trend,
    List<PerformanceMetric> metrics,
    List<String> strengths,
    List<String> improvements,
    Map<String, Double> comparison
) {}
