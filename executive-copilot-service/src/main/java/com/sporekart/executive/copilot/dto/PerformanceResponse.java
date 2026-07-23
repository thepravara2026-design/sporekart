package com.sporekart.executive.copilot.dto;

import java.util.List;
import java.util.Map;

public record PerformanceResponse(
    List<DepartmentSummary> departments,
    Map<String, Object> crossDepartmental
) {
    public record DepartmentSummary(String name, double score, double trend, List<String> strengths, List<String> improvements) {}
}
