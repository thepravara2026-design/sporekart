package com.sporekart.bi.copilot.domain;

import java.util.List;

public record CustomerSegment(
    String segmentId,
    String name,
    String description,
    int customerCount,
    double totalRevenue,
    double averageRevenue,
    double churnRate,
    List<String> characteristics,
    List<String> recommendedStrategies
) {}
