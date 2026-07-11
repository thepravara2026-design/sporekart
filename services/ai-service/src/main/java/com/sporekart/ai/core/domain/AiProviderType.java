package com.sporekart.ai.core.domain;

public enum AiProviderType {
    GEMINI("Gemini", "google"),
    OPENAI("OpenAI", "openai"),
    CLAUDE("Claude", "anthropic"),
    AZURE_OPENAI("Azure OpenAI", "microsoft"),
    LOCAL("Local", "local"),
    MOCK("Mock", "test");

    private final String displayName;
    private final String vendor;

    AiProviderType(String displayName, String vendor) {
        this.displayName = displayName;
        this.vendor = vendor;
    }

    public String getDisplayName() { return displayName; }
    public String getVendor() { return vendor; }
}
