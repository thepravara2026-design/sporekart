package com.sporekart.bi.copilot.dto;

import java.util.List;
import java.util.Map;

import com.sporekart.bi.copilot.domain.TrendDataPoint;

public record TrendsResponse(
    List<TrendDataPoint> trends,
    String metric,
    String period,
    int dataPoints,
    Map<String, Object> summary
) {}
