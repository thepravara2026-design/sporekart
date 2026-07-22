package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.DocumentIngestionService;
import com.sporekart.ai.knowledge.api.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.api.KnowledgeDocumentRepository;
import com.sporekart.ai.knowledge.api.KnowledgeSourceRepository;
import com.sporekart.ai.knowledge.domain.*;
import java.time.Instant;
import java.util.*;

public class DocumentIngestionManager implements DocumentIngestionService {
    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeChunkRepository chunkRepository;
    private final KnowledgeSourceRepository sourceRepository;
    private final DocumentChunkingEngine chunkingEngine;

    public DocumentIngestionManager(KnowledgeDocumentRepository documentRepository,
                                    KnowledgeChunkRepository chunkRepository,
                                    KnowledgeSourceRepository sourceRepository,
                                    DocumentChunkingEngine chunkingEngine) {
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.sourceRepository = sourceRepository;
        this.chunkingEngine = chunkingEngine;
    }

    @Override
    public KnowledgeDocument ingestDocument(KnowledgeSourceId sourceId, String title,
                                            DocumentType documentType, String content,
                                            String author, String language) {
        var id = KnowledgeDocumentId.random();
        var now = Instant.now();
        var checksum = Integer.toString(content.hashCode());
        var doc = new KnowledgeDocument(id, sourceId, null, title, documentType, content,
                new HashMap<>(), "1.0", DocumentStatus.DRAFT, checksum,
                content.length(), author, language, 0, now, now);
        return documentRepository.save(doc);
    }

    @Override
    public KnowledgeDocument replaceDocument(KnowledgeDocumentId id, String content, String title) {
        var existing = getDocument(id);
        var now = Instant.now();
        var checksum = Integer.toString(content.hashCode());
        var doc = new KnowledgeDocument(existing.id(), existing.sourceId(), existing.collectionId(),
                title != null ? title : existing.title(), existing.documentType(), content,
                existing.metadata(), existing.version(), existing.status(), checksum,
                content.length(), existing.author(), existing.language(), existing.chunkCount(),
                existing.createdAt(), now);
        return documentRepository.save(doc);
    }

    @Override
    public KnowledgeDocument getDocument(KnowledgeDocumentId id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + id));
    }

    @Override
    public List<KnowledgeDocument> listDocuments(KnowledgeSourceId sourceId, DocumentStatus status) {
        var all = sourceId != null
                ? documentRepository.findBySourceId(sourceId)
                : documentRepository.findAll();
        if (status != null) {
            all = all.stream().filter(d -> d.status() == status).toList();
        }
        return all;
    }

    @Override
    public void archiveDocument(KnowledgeDocumentId id) {
        var doc = getDocument(id);
        doc.archive();
        documentRepository.save(doc);
    }

    @Override
    public void publishDocument(KnowledgeDocumentId id) {
        var doc = getDocument(id);
        doc.publish();
        documentRepository.save(doc);
    }

    @Override
    public void deleteDocument(KnowledgeDocumentId id) {
        var doc = getDocument(id);
        doc.markDeleted();
        documentRepository.save(doc);
    }

    @Override
    public List<KnowledgeChunk> getChunks(KnowledgeDocumentId documentId) {
        return chunkRepository.findByDocumentIdOrderBySequence(documentId);
    }

    @Override
    public List<ChunkResult> chunkDocument(KnowledgeDocumentId documentId, ChunkStrategy strategy,
                                           int maxChunkSize, int overlap) {
        var doc = getDocument(documentId);
        var effectiveSize = maxChunkSize > 0 ? maxChunkSize : 1000;
        var effectiveOverlap = overlap >= 0 ? overlap : 100;

        var chunkResults = chunkingEngine.chunk(doc.content(), strategy, effectiveSize, effectiveOverlap);
        var chunks = new ArrayList<KnowledgeChunk>();
        var now = Instant.now();

        for (int i = 0; i < chunkResults.size(); i++) {
            var cr = chunkResults.get(i);
            var chunkId = KnowledgeChunkId.random();
            var chunk = new KnowledgeChunk(chunkId, documentId, cr.content(), i, i,
                    cr.tokens(), cr.heading(), cr.section(), new HashMap<>(), null, now);
            chunks.add(chunk);
        }

        chunkRepository.deleteByDocumentId(documentId);
        chunkRepository.saveAll(chunks);
        doc.setChunkCount(chunks.size());
        documentRepository.save(doc);

        return chunkResults;
    }
}
