package com.sporekart.ai.pipeline.normalization;

import com.sporekart.ai.pipeline.model.PipelineResponse;

import java.util.Map;

public class ResponseNormalizer {

    public NormalizedResponse normalize(PipelineResponse response) {
        if (response == null) {
            return NormalizedResponse.empty();
        }

        var normalizedText = normalizeGeneratedText(response.generatedOutput());
        var normalizedStructured = normalizeStructuredOutput(response.structuredOutput());
        var normalizedTokens = response.tokenUsage();
        var normalizedCost = normalizeCost(response.cost(), response.provider());
        var standardizedFinishReason = standardizeFinishReason(response.finishReason());
        var normalizedWarnings = normalizeWarnings(response.warnings());

        return new NormalizedResponse(
                normalizedText,
                normalizedStructured,
                response.provider(),
                response.model(),
                normalizedTokens,
                normalizedCost,
                standardizedFinishReason,
                normalizedWarnings,
                response.success(),
                response.metadata());
    }

    String normalizeGeneratedText(String text) {
        if (text == null) return "";
        return text.trim();
    }

    String normalizeStructuredOutput(String structured) {
        if (structured == null) return "";
        return structured.trim();
    }

    double normalizeCost(double cost, String provider) {
        if (cost < 0) return 0.0;
        return Math.round(cost * 1_000_000.0) / 1_000_000.0;
    }

    String standardizeFinishReason(com.sporekart.ai.pipeline.model.FinishReason reason) {
        if (reason == null) return "unknown";
        return switch (reason) {
            case STOP -> "stop";
            case LENGTH -> "length";
            case CONTENT_FILTER -> "content_filter";
            case TOOL_CALLS -> "tool_calls";
            case FUNCTION_CALL -> "function_call";
            case ERROR -> "error";
            case TIMEOUT -> "timeout";
            case CANCELLED -> "cancelled";
            case UNKNOWN -> "unknown";
        };
    }

    java.util.List<String> normalizeWarnings(java.util.List<String> warnings) {
        if (warnings == null) return java.util.List.of();
        return warnings.stream().map(String::trim).filter(w -> !w.isEmpty()).toList();
    }

    public record NormalizedResponse(
            String generatedText,
            String structuredOutput,
            String provider,
            String model,
            com.sporekart.ai.pipeline.model.TokenUsage tokenUsage,
            double normalizedCost,
            String finishReason,
            java.util.List<String> warnings,
            boolean success,
            Map<String, Object> metadata) {

        public static NormalizedResponse empty() {
            return new NormalizedResponse("", "", null, null,
                    com.sporekart.ai.pipeline.model.TokenUsage.EMPTY, 0.0, "unknown",
                    java.util.List.of(), false, Map.of());
        }
    }
}
