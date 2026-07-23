package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record WhatsAppRequest(
    @NotBlank String messageType,
    @NotBlank String content,
    String recipientSegment,
    String campaignId,
    String callToAction
) {}
