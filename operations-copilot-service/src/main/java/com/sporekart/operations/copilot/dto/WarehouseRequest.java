package com.sporekart.operations.copilot.dto;

import jakarta.validation.constraints.NotBlank;

public record WarehouseRequest(
    @NotBlank String query,
    @NotBlank String intent,
    String warehouseId
) {}
