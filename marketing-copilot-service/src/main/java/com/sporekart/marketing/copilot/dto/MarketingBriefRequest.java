package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record MarketingBriefRequest(
    @NotBlank String objective,
    @NotBlank String targetAudience,
    String keyMessage,
    List<String> channels,
    double budget,
    String timeline
) {}
