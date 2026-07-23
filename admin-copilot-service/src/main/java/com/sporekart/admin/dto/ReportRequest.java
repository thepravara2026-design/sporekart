package com.sporekart.admin.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public record ReportRequest(
    @NotBlank String title,
    String type,
    LocalDate periodFrom,
    LocalDate periodTo,
    List<String> metrics,
    String format
) {}
