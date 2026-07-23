package com.sporekart.executive.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record ReportRequest(
    @NotBlank String reportType,
    String period
) {}
