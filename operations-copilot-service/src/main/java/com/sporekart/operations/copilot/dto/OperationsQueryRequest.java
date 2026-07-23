package com.sporekart.operations.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record OperationsQueryRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String warehouseId,
    String region,
    String category
) {}
