package com.sporekart.prompt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record TestExecutionRequest(
        @NotNull UUID versionId,
        Map<String, String> variables,
        @NotBlank String provider,
        @NotBlank String model,
        UUID userId
) {}
