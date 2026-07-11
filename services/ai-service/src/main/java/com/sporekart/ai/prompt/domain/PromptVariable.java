package com.sporekart.ai.prompt.domain;

public record PromptVariable(
        String name,
        String type,
        boolean required,
        String defaultValue,
        String description) {
}
