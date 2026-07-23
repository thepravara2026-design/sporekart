package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ContentGenerationRequest(
    @NotBlank String contentType,
    @NotBlank String topic,
    String audience,
    String tone,
    String locale,
    List<String> keywords,
    Integer wordCount,
    String campaignId,
    String brandVoice
) {}
