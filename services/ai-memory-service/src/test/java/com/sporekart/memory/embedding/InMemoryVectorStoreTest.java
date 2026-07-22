package com.sporekart.memory.embedding;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class InMemoryVectorStoreTest {

    @Autowired
    private InMemoryVectorStore store;

    @Test
    void shouldStoreAndRetrieve() {
        var id = java.util.UUID.randomUUID();
        var vector = new float[]{1.0f, 2.0f, 3.0f};
        store.store(id, vector, "test-metadata");

        var retrieved = store.retrieve(id);
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get()).containsExactly(1.0f, 2.0f, 3.0f);
    }

    @Test
    void shouldDelete() {
        var id = java.util.UUID.randomUUID();
        store.store(id, new float[]{1.0f}, "to-delete");
        store.delete(id);

        assertThat(store.retrieve(id)).isEmpty();
    }

    @Test
    void shouldSearch() {
        store.store(java.util.UUID.randomUUID(), new float[]{1.0f, 0.0f, 0.0f}, "item1");
        store.store(java.util.UUID.randomUUID(), new float[]{0.0f, 1.0f, 0.0f}, "item2");

        var results = store.search(new float[]{1.0f, 0.0f, 0.0f}, 10, 0.0);
        assertThat(results).hasSize(2);
        assertThat(results.get(0).metadata()).isEqualTo("item1");
    }
}
