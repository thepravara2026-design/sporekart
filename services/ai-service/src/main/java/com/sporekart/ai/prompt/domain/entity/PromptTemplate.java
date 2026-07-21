package com.sporekart.ai.prompt.domain.entity;

import com.sporekart.ai.prompt.domain.valueobject.*;
import java.time.Instant;
import java.util.*;

public class PromptTemplate {
    private PromptType type;
    private String rawContent;
    private List<TemplateSection> sections;
    private List<PromptVariable> variables;
    private PromptExecutionPolicy executionPolicy;

    public PromptTemplate(PromptType type, String rawContent, List<TemplateSection> sections,
                         List<PromptVariable> variables, PromptExecutionPolicy executionPolicy) {
        this.type = Objects.requireNonNull(type, "Template type must not be null");
        this.rawContent = Objects.requireNonNull(rawContent, "Raw content must not be null");
        this.sections = sections == null ? List.of() : List.copyOf(sections);
        this.variables = variables == null ? List.of() : List.copyOf(variables);
        this.executionPolicy = executionPolicy == null ? PromptExecutionPolicy.defaults() : executionPolicy;
    }

    public PromptType type() { return type; }
    public String rawContent() { return rawContent; }
    public List<TemplateSection> sections() { return List.copyOf(sections); }
    public List<PromptVariable> variables() { return List.copyOf(variables); }
    public PromptExecutionPolicy executionPolicy() { return executionPolicy; }

    public PromptVariable findVariable(String name) {
        return variables.stream().filter(v -> v.name().equals(name)).findFirst().orElse(null);
    }

    public boolean hasRequiredVariables() {
        return variables.stream().anyMatch(PromptVariable::required);
    }

    public boolean isValid() {
        return rawContent != null && !rawContent.isBlank() && type != null;
    }

    public void validateContent() {
        if (rawContent == null || rawContent.isBlank()) {
            throw new IllegalStateException("Template raw content must not be empty");
        }
        for (var section : sections) {
            if (section.content() == null || section.content().isBlank()) {
                throw new IllegalStateException("Section " + section.type() + " content must not be empty");
            }
        }
    }

    public PromptTemplate withUpdatedContent(String rawContent, List<TemplateSection> sections) {
        return new PromptTemplate(type, rawContent, sections, variables, executionPolicy);
    }

    public PromptTemplate withUpdatedVariables(List<PromptVariable> variables) {
        return new PromptTemplate(type, rawContent, sections, variables, executionPolicy);
    }

    public PromptTemplate withUpdatedPolicy(PromptExecutionPolicy executionPolicy) {
        return new PromptTemplate(type, rawContent, sections, variables, executionPolicy);
    }

    public Set<String> collectVariableNames() {
        var names = new HashSet<String>();
        for (var variable : variables) {
            names.add(variable.name());
        }
        return names;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromptTemplate that)) return false;
        return type == that.type && Objects.equals(rawContent, that.rawContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, rawContent);
    }
}
