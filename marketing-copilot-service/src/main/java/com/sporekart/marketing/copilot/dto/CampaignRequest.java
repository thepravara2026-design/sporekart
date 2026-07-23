package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

public record CampaignRequest(
    @NotBlank String name,
    @NotBlank String type,
    @NotBlank String objective,
    String targetAudience,
    LocalDate startDate,
    LocalDate endDate,
    @Positive double budget,
    List<String> channels,
    String brandVoice
) {}
