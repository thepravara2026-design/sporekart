package com.sporekart.operations.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderOperationsRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String orderId,
    String warehouseId,
    String status
) {}
