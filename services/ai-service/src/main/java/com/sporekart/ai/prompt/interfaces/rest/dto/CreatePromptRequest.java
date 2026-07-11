package com.sporekart.ai.prompt.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(description = "Request to create a new prompt template")
public record CreatePromptRequest(
        @NotNull UUID categoryId,
        @NotBlank String name,
        String description,
        @NotBlank String templateText,
        List<VariableInput> variables) {

    @Schema(description = "Variable definition for a prompt template")
    public record VariableInput(
            @NotBlank String name,
            String type,
            boolean required,
            String defaultValue,
            String description,
            String validationRegex) {}
}
