package com.sporekart.catalog.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank @Size(max = 100) String sku,
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) String slug,
        @Size(max = 4000) String description) {
}
