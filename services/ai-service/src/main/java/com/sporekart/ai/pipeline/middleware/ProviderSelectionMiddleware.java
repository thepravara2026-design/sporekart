package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;

import java.util.List;
import java.util.Random;

public class ProviderSelectionMiddleware implements Middleware {
    private static final List<String> DEFAULT_PROVIDERS = List.of("OPENAI", "GEMINI", "CLAUDE");
    private static final List<String> FALLBACK_ORDER = List.of("GEMINI", "OPENAI", "CLAUDE", "GROQ");

    @Override
    public String name() { return "ProviderSelection"; }

    @Override
    public int order() { return 60; }

    @Override
    public void execute(PipelineContext context, MiddlewareChain chain) {
        var request = context.request();

        String selectedProvider = null;
        String selectedModel = null;

        if (request.providerPreference() != null && !request.providerPreference().isBlank()) {
            selectedProvider = request.providerPreference();
        } else {
            selectedProvider = FALLBACK_ORDER.get(0);
        }

        if (request.model() != null && !request.model().isBlank()) {
            selectedModel = request.model();
        } else {
            selectedModel = switch (selectedProvider.toUpperCase()) {
                case "OPENAI" -> "gpt-4";
                case "GEMINI" -> "gemini-pro";
                case "CLAUDE" -> "claude-3-sonnet";
                case "AZURE_OPENAI" -> "gpt-4";
                case "OLLAMA" -> "llama3";
                case "GROQ" -> "mixtral-8x7b";
                case "MISTRAL" -> "mistral-large";
                case "OPENROUTER" -> "gpt-4";
                case "BEDROCK" -> "anthropic.claude-v2";
                case "TOGETHER_AI" -> "mixtral-8x7b";
                default -> "gpt-4";
            };
        }

        context.selectProvider(selectedProvider, selectedModel);
        context.setAttribute("selectionStrategy", "first-available");
        context.recordMiddleware(name());
        chain.next(context);
    }
}
