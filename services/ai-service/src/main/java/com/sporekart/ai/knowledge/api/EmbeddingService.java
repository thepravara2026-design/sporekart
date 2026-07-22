package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface EmbeddingService {
    List<Float> embed(String text, EmbeddingProvider provider);
    List<List<Float>> embedBatch(List<String> texts, EmbeddingProvider provider);
    void indexDocument(KnowledgeDocumentId documentId, EmbeddingProvider embeddingProvider, VectorStoreProvider vectorStore);
    void reindexDocument(KnowledgeDocumentId documentId, EmbeddingProvider embeddingProvider, VectorStoreProvider vectorStore);
    void reindexAll(EmbeddingProvider embeddingProvider, VectorStoreProvider vectorStore);
}
