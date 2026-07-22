package com.sporekart.prompt.dto.response;

import com.sporekart.prompt.domain.PromptCategory;
import com.sporekart.prompt.domain.PromptScope;
import com.sporekart.prompt.domain.PromptStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record PromptTemplateResponse(
        UUID id,
        String name,
        String slug,
        String description,
        PromptCategory category,
        PromptScope scope,
        PromptStatus status,
        UUID owner,
        UUID createdBy,
        UUID updatedBy,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        boolean isActive,
        List<String> tags,
        Integer latestVersion,
        UUID latestVersionId,
        boolean isPublished
) {
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private String name;
        private String slug;
        private String description;
        private PromptCategory category;
        private PromptScope scope;
        private PromptStatus status;
        private UUID owner;
        private UUID createdBy;
        private UUID updatedBy;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private boolean isActive;
        private List<String> tags;
        private Integer latestVersion;
        private UUID latestVersionId;
        private boolean isPublished;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder slug(String slug) { this.slug = slug; return this; }
        public Builder description(String desc) { this.description = desc; return this; }
        public Builder category(PromptCategory c) { this.category = c; return this; }
        public Builder scope(PromptScope s) { this.scope = s; return this; }
        public Builder status(PromptStatus s) { this.status = s; return this; }
        public Builder owner(UUID o) { this.owner = o; return this; }
        public Builder createdBy(UUID c) { this.createdBy = c; return this; }
        public Builder updatedBy(UUID u) { this.updatedBy = u; return this; }
        public Builder createdAt(OffsetDateTime c) { this.createdAt = c; return this; }
        public Builder updatedAt(OffsetDateTime u) { this.updatedAt = u; return this; }
        public Builder isActive(boolean a) { this.isActive = a; return this; }
        public Builder tags(List<String> t) { this.tags = t; return this; }
        public Builder latestVersion(Integer v) { this.latestVersion = v; return this; }
        public Builder latestVersionId(UUID v) { this.latestVersionId = v; return this; }
        public Builder isPublished(boolean p) { this.isPublished = p; return this; }

        public PromptTemplateResponse build() {
            return new PromptTemplateResponse(id, name, slug, description, category, scope, status,
                    owner, createdBy, updatedBy, createdAt, updatedAt, isActive, tags, latestVersion, latestVersionId, isPublished);
        }
    }
}
