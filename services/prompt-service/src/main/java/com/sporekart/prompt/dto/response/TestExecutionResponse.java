package com.sporekart.prompt.dto.response;

import java.math.BigDecimal;

public record TestExecutionResponse(
        String renderedPrompt,
        String response,
        BigDecimal cost,
        int promptTokens,
        int completionTokens,
        int totalTokens,
        long latencyMs,
        boolean success,
        String error
) {}
