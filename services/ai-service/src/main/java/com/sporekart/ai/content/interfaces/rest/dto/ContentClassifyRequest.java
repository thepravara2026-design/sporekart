package com.sporekart.ai.content.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ContentClassifyRequest(
        @NotBlank String text,
        List<String> categories,
        int maxCategories
) {}
