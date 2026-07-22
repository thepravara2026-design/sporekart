package com.sporekart.prompt.dto.request;

import com.sporekart.prompt.domain.PromptCategory;
import com.sporekart.prompt.domain.PromptScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreatePromptRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) String slug,
        @Size(max = 5000) String description,
        @NotNull PromptCategory category,
        @NotNull PromptScope scope,
        @NotNull UUID owner,
        @NotNull UUID createdBy,
        List<String> tags
) {}
