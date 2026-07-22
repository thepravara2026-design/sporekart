package com.sporekart.ai.conversation.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.conversation.domain.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ContextWindowManager")
class ContextWindowManagerTest {

    private ContextWindowManager manager;

    @BeforeEach
    void setUp() {
        manager = new ContextWindowManager();
    }

    private Message createMessage(String role, String content) {
        return new Message(MessageId.random(), ConversationId.random(), MessageType.USER, content, role,
            Map.of(), List.of(), List.of(), null, null, null, null, null, "active", Instant.now(), Instant.now());
    }

    @Test
    @DisplayName("should build a context window with the same number of messages")
    void shouldBuildWindowFromMessages() {
        var convId = ConversationId.random();
        var messages = List.of(
            createMessage("system", "Be helpful"),
            createMessage("user", "Hello"),
            createMessage("assistant", "Hi there")
        );

        var window = manager.buildWindow(convId, messages);

        assertEquals(3, window.getActiveMessages().size());
        assertEquals(convId.toString(), window.getConversationId());
    }

    @Test
    @DisplayName("should compress window keeping system and last N messages")
    void shouldCompressWindow() {
        var messages = new java.util.ArrayList<Message>();
        messages.add(createMessage("system", "System prompt"));
        for (int i = 0; i < 30; i++) {
            messages.add(createMessage("user", "Message " + i));
        }
        var window = manager.buildWindow(ConversationId.random(), messages);

        var compressed = manager.compress(window);

        assertTrue(compressed.getActiveMessages().size() < window.getActiveMessages().size());
        assertEquals(1, compressed.getCompressionCount());
    }

    @Test
    @DisplayName("should trim window to fit within token limit")
    void shouldTrimWindowToTokenLimit() {
        var messages = new java.util.ArrayList<Message>();
        messages.add(createMessage("system", "Sys"));
        for (int i = 0; i < 10; i++) {
            messages.add(createMessage("user", "A".repeat(200)));
        }
        var window = manager.buildWindow(ConversationId.random(), messages);

        var trimmed = manager.trim(window, 50);

        assertTrue(trimmed.getTotalTokens() <= 50);
    }

    @Test
    @DisplayName("should prioritize messages containing priority keywords")
    void shouldPrioritizeMessages() {
        var convId = ConversationId.random();
        var msg1 = createMessage("user", "Urgent: fix the bug");
        var msg2 = createMessage("user", "Hello");
        var msg3 = createMessage("user", "Urgent: deploy now");
        var window = manager.buildWindow(convId, List.of(msg1, msg2, msg3));

        var prioritized = manager.prioritize(window, List.of("Urgent"));

        assertEquals("Urgent: fix the bug", prioritized.getActiveMessages().get(0).getContent());
        assertEquals("Urgent: deploy now", prioritized.getActiveMessages().get(1).getContent());
        assertEquals("Hello", prioritized.getActiveMessages().get(2).getContent());
    }

    @Test
    @DisplayName("should detect when compression is needed")
    void shouldDetectWhenCompressionNeeded() {
        var messages = List.of(
            createMessage("user", "A".repeat(7000)),
            createMessage("assistant", "B".repeat(7000))
        );
        var window = manager.buildWindow(ConversationId.random(), messages);

        assertTrue(manager.needsCompression(window));
    }

    @Test
    @DisplayName("should handle empty message list with zero tokens")
    void shouldHandleEmptyMessages() {
        var window = manager.buildWindow(ConversationId.random(), List.of());

        assertEquals(0, window.getActiveMessages().size());
        assertEquals(Integer.valueOf(0), window.getTotalTokens());
    }
}
