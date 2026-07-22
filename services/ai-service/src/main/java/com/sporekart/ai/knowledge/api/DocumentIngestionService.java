package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface DocumentIngestionService {
    KnowledgeDocument ingestDocument(KnowledgeSourceId sourceId, String title, DocumentType documentType, String content, String author, String language);
    KnowledgeDocument replaceDocument(KnowledgeDocumentId id, String content, String title);
    KnowledgeDocument getDocument(KnowledgeDocumentId id);
    List<KnowledgeDocument> listDocuments(KnowledgeSourceId sourceId, DocumentStatus status);
    void archiveDocument(KnowledgeDocumentId id);
    void publishDocument(KnowledgeDocumentId id);
    void deleteDocument(KnowledgeDocumentId id);
    List<KnowledgeChunk> getChunks(KnowledgeDocumentId documentId);
    List<ChunkResult> chunkDocument(KnowledgeDocumentId documentId, ChunkStrategy strategy, int maxChunkSize, int overlap);
}
