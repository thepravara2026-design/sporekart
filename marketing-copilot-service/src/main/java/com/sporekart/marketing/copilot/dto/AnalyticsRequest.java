package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalyticsRequest(
    @NotBlank String campaignId,
    String startDate,
    String endDate
) {}
