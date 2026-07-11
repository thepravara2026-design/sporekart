package com.sporekart.ai.prompt.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record PromptTemplate(
        String id,
        String name,
        String category,
        String template,
        String version,
        List<PromptVariable> variables,
        String status,
        String createdBy,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public PromptTemplate(String name, String category, String template, String version) {
        this(null, name, category, template, version, List.of(), "DRAFT", null, OffsetDateTime.now(), OffsetDateTime.now());
    }

    public String render(Map<String, Object> variableValues) {
        String result = template;
        for (PromptVariable variable : variables) {
            Object value = variableValues.getOrDefault(variable.name(), variable.defaultValue() != null ? variable.defaultValue() : "");
            result = result.replace("{{" + variable.name() + "}}", String.valueOf(value));
        }
        return result;
    }
}
