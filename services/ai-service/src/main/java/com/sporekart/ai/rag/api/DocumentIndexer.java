package com.sporekart.ai.rag.api;

import com.sporekart.ai.rag.domain.RagDocument;

public interface DocumentIndexer {
    String indexDocument(RagDocument document);
    void removeDocument(String documentId);
    void reindexAll();
}
