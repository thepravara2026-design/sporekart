package com.sporekart.prompt.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RollbackRequest(
        @NotNull UUID templateId,
        @NotNull Integer targetVersion,
        @NotNull UUID performedBy,
        String reason
) {}
