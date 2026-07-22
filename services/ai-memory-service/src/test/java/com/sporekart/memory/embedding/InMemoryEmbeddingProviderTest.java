package com.sporekart.memory.embedding;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class InMemoryEmbeddingProviderTest {

    @Autowired
    private InMemoryEmbeddingProvider provider;

    @Test
    void shouldGenerateEmbedding() {
        var result = provider.generateEmbedding("Hello world");
        assertThat(result.vector()).hasSize(1536);
        assertThat(result.embeddingId()).isNotNull();
    }

    @Test
    void shouldSearchSimilar() {
        provider.generateEmbedding("Test content");
        var vector = new float[1536];
        vector[0] = 1.0f;

        var results = provider.searchSimilar(vector, 10, 0.0);
        assertThat(results).isNotEmpty();
    }
}
