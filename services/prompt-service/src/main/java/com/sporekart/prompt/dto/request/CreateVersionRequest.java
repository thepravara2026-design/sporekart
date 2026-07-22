package com.sporekart.prompt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateVersionRequest(
        @NotBlank String promptBody,
        String systemPrompt,
        String developerPrompt,
        String userPrompt,
        String fewShotExamples,
        String conversationInstructions,
        String safetyConstraints,
        String providerMetadata,
        String variablesJson,
        String providerConstraints,
        BigDecimal temperature,
        BigDecimal topP,
        Integer maxTokens,
        @NotNull UUID createdBy,
        String changeNotes
) {}
