package com.sporekart.prompt.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ApprovalRequest(
        @NotNull UUID versionId,
        @NotNull UUID templateId,
        @NotNull UUID approver,
        @NotNull String step,
        String comments
) {}
