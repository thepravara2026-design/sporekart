package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentChunkingEngineTest {

    private final DocumentChunkingEngine engine = new DocumentChunkingEngine();

    @Test
    void shouldChunkFixedSize() {
        var content = "a".repeat(1000);
        var chunks = engine.chunk(content, ChunkStrategy.FIXED_SIZE, 200, 50);

        assertFalse(chunks.isEmpty());

        var overlapFound = false;
        for (int i = 1; i < chunks.size(); i++) {
            var prev = chunks.get(i - 1).content();
            var curr = chunks.get(i).content();
            if (prev.length() > 50 && curr.length() > 50) {
                var overlap = prev.substring(prev.length() - 50);
                if (curr.startsWith(overlap)) {
                    overlapFound = true;
                    break;
                }
            }
        }
        assertTrue(overlapFound, "Expected overlapping content between chunks");
    }

    @Test
    void shouldChunkSlidingWindow() {
        var content = "Sliding window chunking test content. ".repeat(50);
        var chunks = engine.chunk(content, ChunkStrategy.SLIDING_WINDOW, 100, 20);

        assertFalse(chunks.isEmpty());
        assertTrue(chunks.size() > 1);
        assertNotNull(chunks.get(0).content());
    }

    @Test
    void shouldChunkBySection() {
        var content = "First paragraph content.\n\nSecond paragraph here.\n\nThird and final paragraph.";
        var chunks = engine.chunk(content, ChunkStrategy.SECTION, 1000, 0);

        assertEquals(3, chunks.size());
        assertTrue(chunks.get(0).content().contains("First"));
        assertTrue(chunks.get(1).content().contains("Second"));
        assertTrue(chunks.get(2).content().contains("Third"));
    }

    @Test
    void shouldChunkByHeading() {
        var content = "# Header One\n\nContent under first header.\n\n## Sub Header\n\nContent under sub header.\n\n# Header Two\n\nFinal section.";
        var chunks = engine.chunk(content, ChunkStrategy.HEADING, 1000, 0);

        assertFalse(chunks.isEmpty());
        var allContent = chunks.stream().map(ChunkResult::content).reduce("", String::concat);
        assertTrue(allContent.contains("Header One") || chunks.stream().anyMatch(c -> "Header One".equals(c.heading())));
        assertTrue(allContent.contains("Header Two") || chunks.stream().anyMatch(c -> "Header Two".equals(c.heading())));
    }

    @Test
    void shouldChunkEmptyContent() {
        var chunks = engine.chunk("", ChunkStrategy.FIXED_SIZE, 100, 0);

        assertTrue(chunks.isEmpty());
    }

    @Test
    void shouldEstimateTokens() {
        var tokens = engine.estimateTokens("hello");

        assertEquals(4, tokens);
    }
}
