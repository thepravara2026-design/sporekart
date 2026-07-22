package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.api.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.api.KnowledgeDocumentRepository;
import com.sporekart.ai.knowledge.api.KnowledgeSourceRepository;
import com.sporekart.ai.knowledge.infrastructure.persistence.InMemoryKnowledgeChunkRepository;
import com.sporekart.ai.knowledge.infrastructure.persistence.InMemoryKnowledgeDocumentRepository;
import com.sporekart.ai.knowledge.infrastructure.persistence.InMemoryKnowledgeSourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DocumentIngestionManagerTest {

    private KnowledgeDocumentRepository documentRepository;
    private KnowledgeChunkRepository chunkRepository;
    private KnowledgeSourceRepository sourceRepository;
    private DocumentChunkingEngine chunkingEngine;
    private DocumentIngestionManager manager;

    @BeforeEach
    void setUp() {
        documentRepository = new InMemoryKnowledgeDocumentRepository();
        chunkRepository = new InMemoryKnowledgeChunkRepository();
        sourceRepository = new InMemoryKnowledgeSourceRepository();
        chunkingEngine = new DocumentChunkingEngine();
        manager = new DocumentIngestionManager(documentRepository, chunkRepository, sourceRepository, chunkingEngine);
    }

    @Test
    void shouldIngestDocument() {
        var sourceId = KnowledgeSourceId.random();
        var result = manager.ingestDocument(sourceId, "Test Doc", DocumentType.TXT, "Content body", "author", "en");

        assertNotNull(result.id());
        assertEquals("Test Doc", result.title());
        assertEquals(DocumentStatus.DRAFT, result.status());
        assertTrue(result.fileSize() > 0);
    }

    @Test
    void shouldGetDocument() {
        var sourceId = KnowledgeSourceId.random();
        var ingested = manager.ingestDocument(sourceId, "Test", DocumentType.TXT, "Content", "author", "en");

        var result = manager.getDocument(ingested.id());

        assertNotNull(result);
        assertEquals(ingested.id(), result.id());
        assertEquals("Test", result.title());
    }

    @Test
    void shouldThrowWhenDocumentNotFound() {
        var id = KnowledgeDocumentId.random();

        assertThrows(NoSuchElementException.class, () -> manager.getDocument(id));
    }

    @Test
    void shouldListDocumentsBySource() {
        var srcA = KnowledgeSourceId.random();
        var srcB = KnowledgeSourceId.random();
        manager.ingestDocument(srcA, "D1", DocumentType.TXT, "Content", "a", "en");
        manager.ingestDocument(srcA, "D2", DocumentType.TXT, "Content", "a", "en");
        manager.ingestDocument(srcB, "D3", DocumentType.TXT, "Content", "a", "en");

        assertEquals(2, manager.listDocuments(srcA, null).size());
        assertEquals(1, manager.listDocuments(srcB, null).size());
    }

    @Test
    void shouldArchiveDocument() {
        var sourceId = KnowledgeSourceId.random();
        var doc = manager.ingestDocument(sourceId, "Doc", DocumentType.TXT, "Content", "author", "en");

        manager.archiveDocument(doc.id());

        var archived = manager.getDocument(doc.id());
        assertEquals(DocumentStatus.ARCHIVED, archived.status());
    }

    @Test
    void shouldPublishDocument() {
        var sourceId = KnowledgeSourceId.random();
        var doc = manager.ingestDocument(sourceId, "Doc", DocumentType.TXT, "Content", "author", "en");

        manager.publishDocument(doc.id());

        var published = manager.getDocument(doc.id());
        assertEquals(DocumentStatus.PUBLISHED, published.status());
    }

    @Test
    void shouldChunkDocumentFixedSize() {
        var sourceId = KnowledgeSourceId.random();
        var doc = manager.ingestDocument(sourceId, "Doc", DocumentType.TXT, "Hello world. ".repeat(100), "author", "en");

        var result = manager.chunkDocument(doc.id(), ChunkStrategy.FIXED_SIZE, 50, 10);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(c -> c.content().contains("Hello")));
    }

    @Test
    void shouldChunkDocumentByHeading() {
        var sourceId = KnowledgeSourceId.random();
        var doc = manager.ingestDocument(sourceId, "Doc", DocumentType.TXT,
                "# Introduction\n\nHello\n\n# Details\n\nWorld", "author", "en");

        var result = manager.chunkDocument(doc.id(), ChunkStrategy.HEADING, 1000, 0);

        assertEquals(2, result.size());
        assertEquals("Introduction", result.get(0).heading());
        assertTrue(result.get(0).content().contains("Hello"));
        assertEquals("Details", result.get(1).heading());
        assertTrue(result.get(1).content().contains("World"));
    }
}
