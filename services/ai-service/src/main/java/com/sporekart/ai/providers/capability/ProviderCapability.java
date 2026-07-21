package com.sporekart.ai.providers.capability;

public enum ProviderCapability {
    CHAT("supports chat completions"),
    COMPLETION("supports text completions"),
    STREAMING("supports streaming responses"),
    VISION("supports image analysis"),
    EMBEDDINGS("supports text embeddings"),
    AUDIO_TRANSCRIPTION("supports audio transcription"),
    AUDIO_SPEECH("supports text-to-speech"),
    IMAGE_GENERATION("supports image generation"),
    FUNCTION_CALLING("supports function/tool calling"),
    JSON_OUTPUT("supports JSON mode output"),
    LONG_CONTEXT("supports long context windows (>32K)"),
    REASONING("supports reasoning models"),
    TOOL_EXECUTION("supports tool execution"),
    FINE_TUNING("supports model fine-tuning"),
    BATCH_API("supports batch API operations"),
    MODERATION("supports content moderation"),
    SAFETY_API("supports safety API endpoints"),
    STRUCTURED_OUTPUT("supports structured JSON output"),
    MULTI_MODAL("supports multiple input modalities"),
    CACHING("supports prompt caching");

    private final String description;

    ProviderCapability(String description) {
        this.description = description;
    }

    public String description() { return description; }
}
