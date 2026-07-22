package com.sporekart.prompt.dto.request;

import com.sporekart.prompt.domain.PromptCategory;
import com.sporekart.prompt.domain.PromptScope;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record UpdatePromptRequest(
        @Size(max = 255) String name,
        @Size(max = 255) String slug,
        @Size(max = 5000) String description,
        PromptCategory category,
        PromptScope scope,
        UUID owner,
        UUID updatedBy,
        List<String> tags
) {}
