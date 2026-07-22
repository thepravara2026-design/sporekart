package com.sporekart.ai.knowledge.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.persistence.InMemoryKnowledgeCollectionRepository;
import com.sporekart.ai.knowledge.infrastructure.persistence.InMemoryKnowledgeSourceRepository;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("KnowledgeRegistryManager")
class KnowledgeRegistryManagerTest {

    private KnowledgeRegistryManager manager;
    private InMemoryKnowledgeSourceRepository sourceRepo;
    private InMemoryKnowledgeCollectionRepository collectionRepo;

    @BeforeEach
    void setUp() {
        sourceRepo = new InMemoryKnowledgeSourceRepository();
        collectionRepo = new InMemoryKnowledgeCollectionRepository();
        manager = new KnowledgeRegistryManager(sourceRepo, collectionRepo);
    }

    @Test
    void shouldRegisterSource() {
        var result = manager.registerSource("Test Source", "description", "workspace-a",
                "owner", DocumentType.TXT, "https://example.com", "en");

        assertNotNull(result.id());
        assertEquals(DocumentStatus.DRAFT, result.status());
        assertEquals("Test Source", result.name());
        assertEquals("workspace-a", result.workspaceId());
    }

    @Test
    void shouldGetSource() {
        var registered = manager.registerSource("Test", "desc", "ws",
                "owner", DocumentType.TXT, null, "en");

        var result = manager.getSource(registered.id());

        assertNotNull(result);
        assertEquals(registered.id(), result.id());
        assertEquals("Test", result.name());
    }

    @Test
    void shouldThrowWhenSourceNotFound() {
        var id = KnowledgeSourceId.random();
        assertThrows(NoSuchElementException.class, () -> manager.getSource(id));
    }

    @Test
    void shouldListSourcesByWorkspace() {
        manager.registerSource("S1", "", "workspace-a", "owner", DocumentType.TXT, null, "en");
        manager.registerSource("S2", "", "workspace-a", "owner", DocumentType.TXT, null, "en");
        manager.registerSource("S3", "", "workspace-b", "owner", DocumentType.TXT, null, "en");

        assertEquals(2, manager.listSources("workspace-a", null).size());
        assertEquals(1, manager.listSources("workspace-b", null).size());
    }

    @Test
    void shouldArchiveSource() {
        var registered = manager.registerSource("Test", "", "ws", "owner", DocumentType.TXT, null, "en");

        manager.archiveSource(registered.id());
        var archived = manager.getSource(registered.id());

        assertEquals(DocumentStatus.ARCHIVED, archived.status());
    }

    @Test
    void shouldPublishSource() {
        var registered = manager.registerSource("Test", "", "ws", "owner", DocumentType.TXT, null, "en");

        manager.publishSource(registered.id());
        var published = manager.getSource(registered.id());

        assertEquals(DocumentStatus.PUBLISHED, published.status());
    }

    @Test
    void shouldCreateCollection() {
        var result = manager.createCollection("Test Collection", "desc", "ws", "owner");

        assertNotNull(result.id());
        assertEquals("Test Collection", result.name());
    }

    @Test
    void shouldListCollections() {
        manager.createCollection("C1", "", "ws-a", "owner");
        manager.createCollection("C2", "", "ws-a", "owner");
        manager.createCollection("C3", "", "ws-b", "owner");

        assertEquals(2, manager.listCollections("ws-a").size());
        assertEquals(1, manager.listCollections("ws-b").size());
    }
}
