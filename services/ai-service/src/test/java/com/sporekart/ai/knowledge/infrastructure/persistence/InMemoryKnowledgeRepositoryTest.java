package com.sporekart.ai.knowledge.infrastructure.persistence;

import com.sporekart.ai.knowledge.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryKnowledgeRepositoryTest {

    private InMemoryKnowledgeSourceRepository sourceRepo;
    private InMemoryKnowledgeDocumentRepository documentRepo;
    private InMemoryKnowledgeChunkRepository chunkRepo;
    private InMemoryKnowledgeCollectionRepository collectionRepo;

    @BeforeEach
    void setUp() {
        sourceRepo = new InMemoryKnowledgeSourceRepository();
        documentRepo = new InMemoryKnowledgeDocumentRepository();
        chunkRepo = new InMemoryKnowledgeChunkRepository();
        collectionRepo = new InMemoryKnowledgeCollectionRepository();
    }

    @Test
    void shouldSaveAndFindSource() {
        var id = KnowledgeSourceId.random();
        var source = new KnowledgeSource(id, "Test Source", "desc", "ws-1",
                "owner", DocumentType.TXT, null, "1.0",
                DocumentStatus.DRAFT, "en", List.of(), null, 30,
                Instant.now(), Instant.now());

        sourceRepo.save(source);

        var found = sourceRepo.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Test Source", found.get().name());
    }

    @Test
    void shouldSaveAndFindDocument() {
        var id = KnowledgeDocumentId.random();
        var sourceId = KnowledgeSourceId.random();
        var doc = new KnowledgeDocument(id, sourceId, null, "Test Doc",
                DocumentType.TXT, "Content", null, "1.0",
                DocumentStatus.DRAFT, "abc", 512L, "author", "en", 0,
                Instant.now(), Instant.now());

        documentRepo.save(doc);

        var found = documentRepo.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Test Doc", found.get().title());
    }

    @Test
    void shouldSaveAndFindChunks() {
        var docId = KnowledgeDocumentId.random();
        var chunkId = KnowledgeChunkId.random();
        var chunk = new KnowledgeChunk(chunkId, docId, "Chunk content", 0, 0, 3,
                null, null, null, null, Instant.now());

        chunkRepo.save(chunk);

        var found = chunkRepo.findById(chunkId);
        assertTrue(found.isPresent());
        assertEquals("Chunk content", found.get().content());

        var byDoc = chunkRepo.findByDocumentId(docId);
        assertEquals(1, byDoc.size());
    }

    @Test
    void shouldSaveAndFindCollection() {
        var id = KnowledgeCollectionId.random();
        var collection = new KnowledgeCollection(id, "Test Collection", "desc", "ws-1",
                "owner", KnowledgePermission.READ, List.of(), null,
                Instant.now(), Instant.now());

        collectionRepo.save(collection);

        var found = collectionRepo.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Test Collection", found.get().name());

        var byWs = collectionRepo.findByWorkspaceId("ws-1");
        assertEquals(1, byWs.size());
    }

    @Test
    void shouldDeleteSource() {
        var id = KnowledgeSourceId.random();
        sourceRepo.save(new KnowledgeSource(id, "Test", null, "ws", null, DocumentType.TXT,
                null, null, DocumentStatus.DRAFT, null, null, null, 0,
                Instant.now(), Instant.now()));

        assertTrue(sourceRepo.exists(id));

        sourceRepo.delete(id);

        assertFalse(sourceRepo.exists(id));
    }

    @Test
    void shouldDeleteDocument() {
        var id = KnowledgeDocumentId.random();
        var sourceId = KnowledgeSourceId.random();
        documentRepo.save(new KnowledgeDocument(id, sourceId, null, "Doc",
                DocumentType.TXT, "content", null, "1.0",
                DocumentStatus.DRAFT, "c", 100L, "a", "en", 0,
                Instant.now(), Instant.now()));

        documentRepo.delete(id);

        assertTrue(documentRepo.findById(id).isEmpty());
    }

    @Test
    void shouldDeleteChunk() {
        var docId = KnowledgeDocumentId.random();
        var id = KnowledgeChunkId.random();
        chunkRepo.save(new KnowledgeChunk(id, docId, "content", 0, 0, 1,
                null, null, null, null, Instant.now()));

        chunkRepo.delete(id);

        assertTrue(chunkRepo.findById(id).isEmpty());
    }

    @Test
    void shouldDeleteCollection() {
        var id = KnowledgeCollectionId.random();
        collectionRepo.save(new KnowledgeCollection(id, "C", null, "ws", null,
                KnowledgePermission.READ, null, null, Instant.now(), Instant.now()));

        collectionRepo.delete(id);

        assertFalse(collectionRepo.exists(id));
    }

    @Test
    void shouldKeepIsolation() {
        var wsA = "workspace-a";
        var wsB = "workspace-b";

        sourceRepo.save(new KnowledgeSource(KnowledgeSourceId.random(), "SA", null, wsA,
                null, DocumentType.TXT, null, null, DocumentStatus.DRAFT,
                null, null, null, 0, Instant.now(), Instant.now()));
        sourceRepo.save(new KnowledgeSource(KnowledgeSourceId.random(), "SB", null, wsB,
                null, DocumentType.TXT, null, null, DocumentStatus.DRAFT,
                null, null, null, 0, Instant.now(), Instant.now()));

        var sourcesInA = sourceRepo.findByWorkspaceId(wsA);
        var sourcesInB = sourceRepo.findByWorkspaceId(wsB);

        assertEquals(1, sourcesInA.size());
        assertEquals(1, sourcesInB.size());
        assertNotEquals(sourcesInA.get(0).id(), sourcesInB.get(0).id());
    }
}
