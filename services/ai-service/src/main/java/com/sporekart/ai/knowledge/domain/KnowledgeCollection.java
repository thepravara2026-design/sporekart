package com.sporekart.ai.knowledge.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class KnowledgeCollection {
    private final KnowledgeCollectionId id;
    private final String name;
    private final String description;
    private final String workspaceId;
    private final String owner;
    private final KnowledgePermission defaultPermission;
    private final List<String> tags;
    private final Map<String, String> policy;
    private final Instant createdAt;
    private Instant updatedAt;

    public KnowledgeCollection(KnowledgeCollectionId id, String name, String description,
                               String workspaceId, String owner,
                               KnowledgePermission defaultPermission, List<String> tags,
                               Map<String, String> policy, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.workspaceId = workspaceId;
        this.owner = owner;
        this.defaultPermission = defaultPermission;
        this.tags = new ArrayList<>(tags != null ? tags : List.of());
        this.policy = policy != null ? Collections.unmodifiableMap(Map.copyOf(policy)) : Map.of();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public KnowledgeCollectionId id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public String workspaceId() { return workspaceId; }
    public String owner() { return owner; }
    public KnowledgePermission defaultPermission() { return defaultPermission; }
    public List<String> tags() { return Collections.unmodifiableList(tags); }
    public Map<String, String> policy() { return policy; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }

    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            updatedAt = Instant.now();
        }
    }

    public void removeTag(String tag) {
        if (tags.remove(tag)) {
            updatedAt = Instant.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeCollection that = (KnowledgeCollection) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "KnowledgeCollection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
