package com.sporekart.ai.prompt.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing the rendered prompt text")
public record RenderPromptResponse(
        String renderedText,
        boolean success,
        String error) {

    public static RenderPromptResponse ok(String renderedText) {
        return new RenderPromptResponse(renderedText, true, null);
    }

    public static RenderPromptResponse error(String error) {
        return new RenderPromptResponse(null, false, error);
    }
}
