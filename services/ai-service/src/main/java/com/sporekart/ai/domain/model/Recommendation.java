package com.sporekart.ai.domain.model;

import java.util.UUID;

public class Recommendation {
    private final UUID id;
    private final String type;
    private final String description;

    public Recommendation(UUID id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
