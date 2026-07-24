package com.sporekart.analytics.infrastructure.persistence;

import com.sporekart.analytics.domain.model.SeoMetadata;
import jakarta.persistence.*;

@Entity
@Table(name = "seo_metadata")
public class SeoMetadataEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "path", nullable = false, unique = true, length = 500)
    private String path;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    protected SeoMetadataEntity() {}

    public SeoMetadataEntity(String id, String path, String title, String description) {
        this.id = id;
        this.path = path;
        this.title = title;
        this.description = description;
    }

    public static SeoMetadataEntity fromDomain(SeoMetadata metadata) {
        return new SeoMetadataEntity(metadata.getId(), metadata.getPath(), metadata.getTitle(), metadata.getDescription());
    }

    public SeoMetadata toDomain() {
        return new SeoMetadata(id, path, title, description);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
