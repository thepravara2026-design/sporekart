package com.sporekart.ai.domain.model;

import java.util.UUID;

public class Prompt {
    private final UUID id;
    private final String category;
    private final String name;
    private final String template;
    private final String version;

    public Prompt(UUID id, String category, String name, String template, String version) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.template = template;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public String getVersion() {
        return version;
    }
}
