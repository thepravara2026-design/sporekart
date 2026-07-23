package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record SocialMediaRequest(
    @NotBlank String platform,
    @NotBlank String topic,
    String postType,
    String tone,
    List<String> hashtags,
    String callToAction,
    String campaignId
) {}
