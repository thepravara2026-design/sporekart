package com.sporekart.bi.copilot.dto;

import java.util.List;

public record ReportRequest(
    String reportType,
    String format,
    String period,
    List<String> sections,
    List<String> recipients
) {}