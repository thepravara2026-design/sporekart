package com.sporekart.ai.prompt.domain.valueobject;

public enum PromptPriority {
    CRITICAL(1),
    HIGH(2),
    MEDIUM(3),
    LOW(4);

    private final int order;

    PromptPriority(int order) { this.order = order; }

    public int order() { return order; }
}
