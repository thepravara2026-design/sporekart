package com.sporekart.operations.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record InventoryQueryRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String warehouseId,
    String category
) {}
