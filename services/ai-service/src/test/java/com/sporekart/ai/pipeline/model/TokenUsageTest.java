package com.sporekart.ai.pipeline.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenUsageTest {
    @Test
    void shouldCreateFromParts() {
        var usage = TokenUsage.of(10, 20);
        assertEquals(10, usage.promptTokens());
        assertEquals(20, usage.completionTokens());
        assertEquals(30, usage.totalTokens());
    }

    @Test
    void shouldThrowOnNegativeTokens() {
        assertThrows(IllegalArgumentException.class, () -> new TokenUsage(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new TokenUsage(0, -1));
    }

    @Test
    void shouldAddUsage() {
        var a = TokenUsage.of(10, 20);
        var b = TokenUsage.of(5, 10);
        var sum = a.add(b);
        assertEquals(15, sum.promptTokens());
        assertEquals(30, sum.completionTokens());
        assertEquals(45, sum.totalTokens());
    }

    @Test
    void emptyIsZero() {
        assertEquals(0, TokenUsage.EMPTY.totalTokens());
    }
}
