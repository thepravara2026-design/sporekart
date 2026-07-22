package com.sporekart.ai.conversation.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.conversation.domain.*;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemoryConversationRepository;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemoryMessageRepository;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MessageEngine")
class MessageEngineTest {

    private MessageEngine engine;
    private InMemoryMessageRepository messageRepo;

    @BeforeEach
    void setUp() {
        messageRepo = new InMemoryMessageRepository();
        var conversationRepo = new InMemoryConversationRepository();
        engine = new MessageEngine(messageRepo, conversationRepo);
    }

    @Test
    @DisplayName("should create a user message with correct type, content, and role")
    void shouldCreateUserMessage() {
        var convId = ConversationId.random();

        var message = engine.createUserMessage(convId, "Hello from user");

        assertEquals(MessageType.USER, message.getType());
        assertEquals("Hello from user", message.getContent());
        assertEquals("user", message.getRole());
    }

    @Test
    @DisplayName("should create a system message with SYSTEM type")
    void shouldCreateSystemMessage() {
        var convId = ConversationId.random();

        var message = engine.createSystemMessage(convId, "System instruction");

        assertEquals(MessageType.SYSTEM, message.getType());
        assertEquals("system", message.getRole());
    }

    @Test
    @DisplayName("should create an assistant message with provider, tokenUsage, and latencyMs")
    void shouldCreateAssistantMessageWithTokens() {
        var convId = ConversationId.random();

        var message = engine.createAssistantMessage(convId, "Assistant response", "gpt-4", 150, 42L);

        assertEquals(MessageType.ASSISTANT, message.getType());
        assertEquals("assistant", message.getRole());
        assertEquals("gpt-4", message.getProviderUsed());
        assertEquals(Integer.valueOf(150), message.getTokenUsage());
        assertEquals(Long.valueOf(42L), message.getLatencyMs());
    }

    @Test
    @DisplayName("should create a tool message with a ToolCall")
    void shouldCreateToolMessage() {
        var convId = ConversationId.random();
        var toolCall = ToolCall.success("get_weather", Map.of("location", "Mumbai"), "Sunny");

        var message = engine.createToolMessage(convId, "Tool result", toolCall);

        assertEquals(MessageType.TOOL, message.getType());
        assertNotNull(message.getToolCall());
        assertEquals("get_weather", message.getToolCall().toolName());
        assertEquals("Sunny", message.getToolCall().result());
    }

    @Test
    @DisplayName("should retrieve messages ordered by createdAt")
    void shouldRetrieveMessagesOrdered() throws InterruptedException {
        var convId = ConversationId.random();
        var msg1 = engine.createUserMessage(convId, "First");
        Thread.sleep(1);
        var msg2 = engine.createUserMessage(convId, "Second");
        Thread.sleep(1);
        var msg3 = engine.createUserMessage(convId, "Third");

        var ordered = engine.getConversationMessages(convId);

        assertEquals(3, ordered.size());
        assertEquals("First", ordered.get(0).getContent());
        assertEquals("Second", ordered.get(1).getContent());
        assertEquals("Third", ordered.get(2).getContent());
    }

    @Test
    @DisplayName("should count messages for a conversation")
    void shouldCountMessages() {
        var convId = ConversationId.random();
        engine.createUserMessage(convId, "A");
        engine.createUserMessage(convId, "B");
        engine.createUserMessage(convId, "C");

        var count = engine.getMessageCount(convId);

        assertEquals(3, count);
    }

    @Test
    @DisplayName("should delete all messages for a conversation")
    void shouldDeleteMessagesForConversation() {
        var convId = ConversationId.random();
        engine.createUserMessage(convId, "Delete me");
        engine.createUserMessage(convId, "Delete me too");

        engine.deleteMessages(convId);

        var remaining = engine.getConversationMessages(convId);
        assertTrue(remaining.isEmpty());
    }
}
