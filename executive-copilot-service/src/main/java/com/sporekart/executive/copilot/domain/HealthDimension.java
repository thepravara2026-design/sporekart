package com.sporekart.executive.copilot.domain;

public record HealthDimension(
    String name,
    double score,
    double previousScore,
    double weight,
    String status,
    String insight
) {}
