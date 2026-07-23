package com.sporekart.admin.domain;

import java.util.List;
import java.util.Map;

public record BusinessInsight(
    String summary,
    Map<String, Object> keyMetrics,
    List<Map<String, Object>> trends,
    List<String> risks,
    List<String> opportunities,
    List<String> recommendations
) {}
