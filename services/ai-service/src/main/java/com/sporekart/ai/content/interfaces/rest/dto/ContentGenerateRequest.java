package com.sporekart.ai.content.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
import java.util.UUID;

public record ContentGenerateRequest(
        @NotBlank @Size(max = 5000) String prompt,
        @NotBlank String contentType,
        String category,
        String tone,
        String targetLanguage,
        int maxLength,
        Map<String, Object> parameters,
        UUID templateId,
        UUID userId
) {}
