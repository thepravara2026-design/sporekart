package com.sporekart.marketing.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandCheckRequest(
    @NotBlank String content,
    String brandVoice,
    String contentType
) {}
