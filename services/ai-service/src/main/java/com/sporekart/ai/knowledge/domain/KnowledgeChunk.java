package com.sporekart.ai.knowledge.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class KnowledgeChunk {
    private final KnowledgeChunkId id;
    private final KnowledgeDocumentId documentId;
    private final String content;
    private final int sequence;
    private final int chunkIndex;
    private int tokens;
    private final String heading;
    private final String section;
    private final Map<String, String> metadata;
    private List<Float> embedding;
    private final Instant createdAt;

    public KnowledgeChunk(KnowledgeChunkId id, KnowledgeDocumentId documentId,
                          String content, int sequence, int chunkIndex, int tokens,
                          String heading, String section, Map<String, String> metadata,
                          List<Float> embedding, Instant createdAt) {
        this.id = id;
        this.documentId = documentId;
        this.content = content;
        this.sequence = sequence;
        this.chunkIndex = chunkIndex;
        this.tokens = tokens;
        this.heading = heading;
        this.section = section;
        this.metadata = metadata != null ? Collections.unmodifiableMap(Map.copyOf(metadata)) : Map.of();
        this.embedding = embedding != null ? List.copyOf(embedding) : null;
        this.createdAt = createdAt;
    }

    public KnowledgeChunkId id() { return id; }
    public KnowledgeDocumentId documentId() { return documentId; }
    public String content() { return content; }
    public int sequence() { return sequence; }
    public int chunkIndex() { return chunkIndex; }
    public int tokens() { return tokens; }
    public String heading() { return heading; }
    public String section() { return section; }
    public Map<String, String> metadata() { return metadata; }
    public List<Float> embedding() { return embedding; }
    public Instant createdAt() { return createdAt; }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding != null ? List.copyOf(embedding) : null;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeChunk that = (KnowledgeChunk) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "KnowledgeChunk{" +
                "id=" + id +
                ", chunkIndex=" + chunkIndex +
                ", heading='" + heading + '\'' +
                '}';
    }
}
