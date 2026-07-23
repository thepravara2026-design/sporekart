package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.SharedMemoryEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SharedMemoryBrokerTest {

    private SharedMemoryBroker memoryBroker;

    @BeforeEach
    void setUp() {
        memoryBroker = new SharedMemoryBroker();
    }

    @Test
    void store_ShouldPersistEntry() {
        SharedMemoryEntry entry = memoryBroker.store(
                SharedMemoryEntry.NAMESPACE_CONVERSATION, "last-topic", "farming tips", "STRING", List.of("farming", "tips"), 30);

        assertNotNull(entry.entryId());
        assertEquals("last-topic", entry.key());
        assertEquals("farming tips", entry.value());
    }

    @Test
    void get_ShouldReturnStoredEntry() {
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "key1", "value1", "STRING", List.of(), 30);
        SharedMemoryEntry entry = memoryBroker.get("key1");

        assertNotNull(entry);
        assertEquals("value1", entry.value());
    }

    @Test
    void getByNamespace_ShouldFilterByNamespace() {
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "conv-key", "conv-val", "STRING", List.of(), 30);
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_KNOWLEDGE, "know-key", "know-val", "STRING", List.of(), 30);

        List<SharedMemoryEntry> convEntries = memoryBroker.getByNamespace(SharedMemoryEntry.NAMESPACE_CONVERSATION);
        assertThat(convEntries).hasSize(1);
        assertThat(convEntries.get(0).key()).isEqualTo("conv-key");
    }

    @Test
    void getByTag_ShouldFilterByTag() {
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "key-tag", "val", "STRING", List.of("urgent", "important"), 30);
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "key-no-tag", "val2", "STRING", List.of("normal"), 30);

        List<SharedMemoryEntry> urgentEntries = memoryBroker.getByTag("urgent");
        assertThat(urgentEntries).hasSize(1);
    }

    @Test
    void search_ShouldFindMatchingEntries() {
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "order-123", "pending", "STRING", List.of(), 30);
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "order-456", "shipped", "STRING", List.of(), 30);
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "invoice-789", "paid", "STRING", List.of(), 30);

        List<SharedMemoryEntry> results = memoryBroker.search("order");
        assertThat(results).hasSize(2);
    }

    @Test
    void delete_ShouldRemoveEntry() {
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "delete-key", "delete-val", "STRING", List.of(), 30);
        memoryBroker.delete("delete-key");

        assertNull(memoryBroker.get("delete-key"));
    }

    @Test
    void expireEntries_ShouldRemoveExpiredEntries() throws Exception {
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "expire-key", "val", "STRING", List.of(), 0);
        Thread.sleep(10);
        memoryBroker.expireEntries();

        assertNull(memoryBroker.get("expire-key"));
    }

    @Test
    void getMemorySummary_ShouldReturnSummary() {
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_CONVERSATION, "k1", "v1", "STRING", List.of(), 30);
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_KNOWLEDGE, "k2", "v2", "STRING", List.of(), 30);
        memoryBroker.store(SharedMemoryEntry.NAMESPACE_WORKSPACE, "k3", "v3", "STRING", List.of(), 30);

        String summary = memoryBroker.getMemorySummary();
        assertThat(summary).contains("CONVERSATION", "KNOWLEDGE", "WORKSPACE");
    }
}
