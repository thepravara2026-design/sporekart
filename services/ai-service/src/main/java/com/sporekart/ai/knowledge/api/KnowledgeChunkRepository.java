package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface KnowledgeChunkRepository {
    Optional<KnowledgeChunk> findById(KnowledgeChunkId id);
    List<KnowledgeChunk> findByDocumentId(KnowledgeDocumentId documentId);
    List<KnowledgeChunk> findByDocumentIdOrderBySequence(KnowledgeDocumentId documentId);
    List<KnowledgeChunk> findByEmbeddingIsNotNull();
    List<KnowledgeChunk> findAll();
    KnowledgeChunk save(KnowledgeChunk chunk);
    List<KnowledgeChunk> saveAll(List<KnowledgeChunk> chunks);
    void delete(KnowledgeChunkId id);
    void deleteByDocumentId(KnowledgeDocumentId documentId);
}
