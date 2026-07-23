package com.sporekart.admin.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Map;

public record AnalyticsQuery(
    @NotBlank String metric,
    @NotBlank String period,
    LocalDate fromDate,
    LocalDate toDate,
    Map<String, String> filters
) {}
