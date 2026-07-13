package com.sporekart.ai.compliance.interfaces.rest.dto;

import java.util.Map;

public record ReportDto(
    String id,
    String frameworkId,
    String title,
    String overallStatus,
    Map<String, Object> summary,
    String generatedAt
) {}
