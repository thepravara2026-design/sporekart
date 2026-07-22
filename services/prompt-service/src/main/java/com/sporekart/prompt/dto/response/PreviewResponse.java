package com.sporekart.prompt.dto.response;

import java.util.List;
import java.util.Map;

public record PreviewResponse(
        String renderedPrompt,
        String systemPrompt,
        Map<String, String> resolvedVariables,
        List<String> missingVariables,
        List<String> warnings,
        TokenEstimate tokenEstimate
) {
    public record TokenEstimate(int estimatedPromptTokens, int estimatedCompletionTokens, int estimatedTotalTokens) {}
}
