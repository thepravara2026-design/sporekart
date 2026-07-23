package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record MarketingQueryRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String audience,
    String campaignId
) {}
