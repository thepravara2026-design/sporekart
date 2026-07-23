package com.sporekart.operations.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record DecisionSupportRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String category,
    String sku
) {}
