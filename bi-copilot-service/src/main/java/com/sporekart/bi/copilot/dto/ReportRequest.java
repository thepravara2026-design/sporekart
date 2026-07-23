package com.sporekart.bi.copilot.dto;

import java.util.List;
import java.util.Map;

public record ReportRequest(
    String reportType,
    String format,
    String schedule,
    List<String> metrics,
    List<String> dimensions,
    Map<String, Object> filters,
    List<String> recipients
) {}
