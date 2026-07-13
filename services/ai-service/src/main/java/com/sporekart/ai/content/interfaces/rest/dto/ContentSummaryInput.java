package com.sporekart.ai.content.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record ContentSummaryInput(
        @NotBlank String text,
        int maxLength,
        String language,
        boolean preserveKeyPoints
) {}
