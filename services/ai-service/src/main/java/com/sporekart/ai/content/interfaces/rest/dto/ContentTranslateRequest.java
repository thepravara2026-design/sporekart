package com.sporekart.ai.content.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record ContentTranslateRequest(
        @NotBlank String text,
        @NotBlank String targetLanguage,
        String sourceLanguage,
        boolean preserveFormatting
) {}
