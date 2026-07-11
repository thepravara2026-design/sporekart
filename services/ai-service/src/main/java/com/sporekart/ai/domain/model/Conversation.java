package com.sporekart.ai.domain.model;

import java.util.UUID;

public class Conversation {
    private final UUID id;
    private final String owner;
    private final String context;

    public Conversation(UUID id, String owner, String context) {
        this.id = id;
        this.owner = owner;
        this.context = context;
    }

    public UUID getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getContext() {
        return context;
    }
}

