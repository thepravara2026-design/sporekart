package com.sporekart.ai.prompt.domain.exception;

public class PromptNotFoundException extends PromptDomainException {
    private final String promptId;

    public PromptNotFoundException(String promptId) {
        super("Prompt not found: " + promptId);
        this.promptId = promptId;
    }

    public String promptId() { return promptId; }
}
