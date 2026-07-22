package com.sporekart.prompt.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ApprovalDecisionRequest(
        @NotNull UUID approvalId,
        @NotNull String decision,
        String comments
) {}
