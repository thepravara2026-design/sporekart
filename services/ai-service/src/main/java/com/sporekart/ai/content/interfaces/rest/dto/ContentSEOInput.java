package com.sporekart.ai.content.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record ContentSEOInput(
        @NotBlank String content,
        @NotBlank String targetKeyword,
        String targetAudience,
        String contentType,
        String language
) {}
