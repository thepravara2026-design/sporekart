package com.sporekart.executive.copilot.domain;

public record PerformanceMetric(
    String name,
    String category,
    double current,
    double previous,
    double target,
    String unit,
    String trend,
    String status
) {}
