package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record EmailRequest(
    @NotBlank String subject,
    @NotBlank String emailType,
    String targetSegment,
    String tone,
    String callToAction,
    String campaignId
) {}
