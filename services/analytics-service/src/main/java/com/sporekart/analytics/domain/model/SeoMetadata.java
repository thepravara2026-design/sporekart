package com.sporekart.analytics.domain.model;

import java.util.UUID;

public class SeoMetadata {
    private final String id;
    private final String path;
    private final String title;
    private final String description;

    public SeoMetadata(String id, String path, String title, String description) {
        this.id = id;
        this.path = path;
        this.title = title;
        this.description = description;
    }

    public static SeoMetadata create(String path, String title, String description) {
        return new SeoMetadata(UUID.randomUUID().toString(), path, title, description);
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
