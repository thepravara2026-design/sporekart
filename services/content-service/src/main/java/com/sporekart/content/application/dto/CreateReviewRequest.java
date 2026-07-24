package com.sporekart.content.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
        @NotBlank String productId,
        @NotBlank String customerId,
        @NotNull @Min(1) @Max(5) Integer rating,
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 4000) String content) {
}
