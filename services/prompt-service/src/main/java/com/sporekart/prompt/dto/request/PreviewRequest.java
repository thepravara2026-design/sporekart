package com.sporekart.prompt.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record PreviewRequest(
        @NotNull UUID versionId,
        Map<String, String> variables
) {}
