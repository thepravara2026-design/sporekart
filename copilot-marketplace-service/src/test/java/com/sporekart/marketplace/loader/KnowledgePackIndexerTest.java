package com.sporekart.marketplace.loader;

import com.sporekart.marketplace.loader.KnowledgePackIndexer.KnowledgePack;
import com.sporekart.marketplace.loader.KnowledgePackIndexer.KnowledgePack.KnowledgePackType;
import com.sporekart.marketplace.registry.CapabilityRegistry;
import com.sporekart.marketplace.sdk.PluginCapability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnowledgePackIndexerTest {

    @Mock
    private CapabilityRegistry capabilityRegistry;

    private KnowledgePackIndexer indexer;

    @BeforeEach
    void setUp() {
        indexer = new KnowledgePackIndexer(capabilityRegistry);
    }

    private KnowledgePack createPack(String packId, String pluginId, KnowledgePackType type) {
        return new KnowledgePack(packId, pluginId, "Pack " + packId,
            "Description for " + packId, type, List.of("doc1.md", "doc2.md"), Instant.now());
    }

    @Test
    void indexPack_shouldStorePackAndRegisterCapability() {
        var pack = createPack("pack-1", "plugin-a", KnowledgePackType.FAQ);
        indexer.indexPack("plugin-a", "Plugin A", pack);

        assertTrue(indexer.getPack("pack-1").isPresent());
        assertEquals("pack-1", indexer.getPack("pack-1").get().packId());
        verify(capabilityRegistry).register("plugin-a", "Plugin A", PluginCapability.KNOWLEDGE);
    }

    @Test
    void getPack_shouldReturnEmptyForUnknownPack() {
        assertFalse(indexer.getPack("unknown").isPresent());
    }

    @Test
    void getPacksByPlugin_shouldReturnOnlyMatchingPacks() {
        var pack1 = createPack("p1", "plugin-a", KnowledgePackType.SOP);
        var pack2 = createPack("p2", "plugin-a", KnowledgePackType.DOMAIN_DOCS);
        var pack3 = createPack("p3", "plugin-b", KnowledgePackType.REFERENCE);

        indexer.indexPack("plugin-a", "Plugin A", pack1);
        indexer.indexPack("plugin-a", "Plugin A", pack2);
        indexer.indexPack("plugin-b", "Plugin B", pack3);

        var pluginAPacks = indexer.getPacksByPlugin("plugin-a");
        assertEquals(2, pluginAPacks.size());
        assertTrue(pluginAPacks.stream().allMatch(p -> p.pluginId().equals("plugin-a")));
    }

    @Test
    void getPacksByPlugin_shouldReturnEmptyWhenNoPacks() {
        assertTrue(indexer.getPacksByPlugin("unknown").isEmpty());
    }

    @Test
    void getAllPacks_shouldReturnAllIndexedPacks() {
        var pack1 = createPack("p1", "plugin-a", KnowledgePackType.FAQ);
        var pack2 = createPack("p2", "plugin-b", KnowledgePackType.TRAINING_MATERIAL);
        indexer.indexPack("plugin-a", "Plugin A", pack1);
        indexer.indexPack("plugin-b", "Plugin B", pack2);

        var all = indexer.getAllPacks();
        assertEquals(2, all.size());
    }

    @Test
    void removePluginPacks_shouldRemoveAllPacksForPlugin() {
        var pack1 = createPack("p1", "plugin-a", KnowledgePackType.FAQ);
        var pack2 = createPack("p2", "plugin-a", KnowledgePackType.REFERENCE);
        var pack3 = createPack("p3", "plugin-b", KnowledgePackType.SOP);
        indexer.indexPack("plugin-a", "Plugin A", pack1);
        indexer.indexPack("plugin-a", "Plugin A", pack2);
        indexer.indexPack("plugin-b", "Plugin B", pack3);

        indexer.removePluginPacks("plugin-a");

        assertEquals(1, indexer.count());
        assertFalse(indexer.getPack("p1").isPresent());
        assertFalse(indexer.getPack("p2").isPresent());
        assertTrue(indexer.getPack("p3").isPresent());
    }

    @Test
    void count_shouldReturnCorrectCount() {
        assertEquals(0, indexer.count());
        indexer.indexPack("plugin-a", "Plugin A", createPack("p1", "plugin-a", KnowledgePackType.FAQ));
        assertEquals(1, indexer.count());
    }

    @Test
    void knowledgePackType_shouldHaveAllExpectedValues() {
        var types = KnowledgePackType.values();
        assertEquals(5, types.length);
        assertTrue(List.of(types).containsAll(List.of(
            KnowledgePackType.FAQ,
            KnowledgePackType.SOP,
            KnowledgePackType.DOMAIN_DOCS,
            KnowledgePackType.TRAINING_MATERIAL,
            KnowledgePackType.REFERENCE
        )));
    }

    @Test
    void indexPack_withAllTypes_shouldStoreEachCorrectly() {
        for (var type : KnowledgePackType.values()) {
            var packId = "pack-" + type.name().toLowerCase();
            var pack = createPack(packId, "plugin-a", type);
            indexer.indexPack("plugin-a", "Plugin A", pack);
        }

        assertEquals(5, indexer.count());

        for (var type : KnowledgePackType.values()) {
            var packId = "pack-" + type.name().toLowerCase();
            var retrieved = indexer.getPack(packId);
            assertTrue(retrieved.isPresent(), "Pack " + packId + " should exist");
            assertEquals(type, retrieved.get().type());
        }
    }

    @Test
    void knowledgePack_shouldPreserveAllFields() {
        var now = Instant.now();
        var documents = List.of("readme.md", "setup.md");
        var pack = new KnowledgePack("kp-1", "plugin-x", "My Pack",
            "A test pack", KnowledgePackType.DOMAIN_DOCS, documents, now);

        assertEquals("kp-1", pack.packId());
        assertEquals("plugin-x", pack.pluginId());
        assertEquals("My Pack", pack.name());
        assertEquals("A test pack", pack.description());
        assertEquals(KnowledgePackType.DOMAIN_DOCS, pack.type());
        assertEquals(documents, pack.documents());
        assertEquals(now, pack.indexedAt());
    }
}
