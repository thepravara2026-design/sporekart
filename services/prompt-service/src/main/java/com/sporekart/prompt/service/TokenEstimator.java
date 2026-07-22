package com.sporekart.prompt.service;

import org.springframework.stereotype.Component;

@Component
public class TokenEstimator {

    private static final double AVG_CHARS_PER_TOKEN_ENGLISH = 4.0;
    private static final double AVG_CHARS_PER_TOKEN_CODE = 3.5;
    private static final double AVG_CHARS_PER_TOKEN_SYSTEM = 4.5;

    public int estimatePromptTokens(String text) {
        if (text == null || text.isBlank()) return 0;
        return (int) Math.ceil(text.length() / AVG_CHARS_PER_TOKEN_ENGLISH);
    }

    public int estimateSystemTokens(String systemPrompt) {
        if (systemPrompt == null || systemPrompt.isBlank()) return 0;
        return (int) Math.ceil(systemPrompt.length() / AVG_CHARS_PER_TOKEN_SYSTEM);
    }

    public int estimateTotalTokens(String promptBody, String systemPrompt, String variablesJson) {
        int bodyTokens = estimatePromptTokens(promptBody);
        int sysTokens = estimateSystemTokens(systemPrompt);
        int varTokens = estimatePromptTokens(variablesJson);
        return bodyTokens + sysTokens + varTokens;
    }

    public int estimateCompletionTokens(String text) {
        if (text == null || text.isBlank()) return 0;
        return (int) Math.ceil(text.length() / AVG_CHARS_PER_TOKEN_CODE);
    }
}
