package com.sporekart.ai.domain.model;

import java.util.UUID;

public class KnowledgeDocument {
    private final UUID id;
    private final String title;
    private final String category;
    private final String content;

    public KnowledgeDocument(UUID id, String title, String category, String content) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }
}

