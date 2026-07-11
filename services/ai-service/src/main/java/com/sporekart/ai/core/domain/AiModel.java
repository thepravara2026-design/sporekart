package com.sporekart.ai.core.domain;

public enum AiModel {
    GEMINI_PRO("gemini-pro", AiProviderType.GEMINI, "chat"),
    GEMINI_PRO_VISION("gemini-pro-vision", AiProviderType.GEMINI, "vision"),
    GPT_4("gpt-4", AiProviderType.OPENAI, "chat"),
    GPT_4_TURBO("gpt-4-turbo", AiProviderType.OPENAI, "chat"),
    GPT_3_5_TURBO("gpt-3.5-turbo", AiProviderType.OPENAI, "chat"),
    CLAUDE_3_HAIKU("claude-3-haiku", AiProviderType.CLAUDE, "chat"),
    CLAUDE_3_SONNET("claude-3-sonnet", AiProviderType.CLAUDE, "chat"),
    CLAUDE_3_OPUS("claude-3-opus", AiProviderType.CLAUDE, "chat"),
    TEXT_EMBEDDING_004("text-embedding-004", AiProviderType.OPENAI, "embedding"),
    MOCK_MODEL("mock-model", AiProviderType.MOCK, "chat");

    private final String modelId;
    private final AiProviderType provider;
    private final String capability;

    AiModel(String modelId, AiProviderType provider, String capability) {
        this.modelId = modelId;
        this.provider = provider;
        this.capability = capability;
    }

    public String getModelId() { return modelId; }
    public AiProviderType getProvider() { return provider; }
    public String getCapability() { return capability; }
}
