package com.sporekart.ai.conversation.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.conversation.domain.*;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemoryMessageRepository;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemorySummaryRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ConversationSummarizer")
class ConversationSummarizerTest {

    private ConversationSummarizer summarizer;
    private InMemorySummaryRepository summaryRepo;
    private InMemoryMessageRepository messageRepo;

    @BeforeEach
    void setUp() {
        summaryRepo = new InMemorySummaryRepository();
        messageRepo = new InMemoryMessageRepository();
        summarizer = new ConversationSummarizer(summaryRepo, messageRepo);
    }

    private Message createMessage(String content) {
        return new Message(MessageId.random(), ConversationId.random(), MessageType.USER, content, "user",
            Map.of(), List.of(), List.of(), null, null, null, null, null, "active", Instant.now(), Instant.now());
    }

    @Test
    @DisplayName("should create summary with non-blank content and conversationId")
    void shouldCreateSummary() {
        var convId = ConversationId.random();
        var messages = List.of(
            createMessage("Hello, I need help with my account"),
            createMessage("Sure, let me check your account details")
        );

        var summary = summarizer.summarize(convId, messages);

        assertNotNull(summary.getSummary());
        assertFalse(summary.getSummary().isBlank());
        assertEquals(convId, summary.getConversationId());
    }

    @Test
    @DisplayName("should create incremental summary combining previous and new messages")
    void shouldCreateIncrementalSummary() {
        var convId = ConversationId.random();
        var initialMessages = List.of(createMessage("First conversation turn"));
        var previous = summarizer.summarize(convId, initialMessages);

        var newMessages = List.of(createMessage("Second conversation turn"));
        var incremental = summarizer.incrementalSummarize(convId, previous, newMessages);

        assertNotNull(incremental);
        assertTrue(incremental.getSummary().contains("First conversation turn"));
        assertTrue(incremental.getSummary().contains("Second conversation turn"));
    }

    @Test
    @DisplayName("should retrieve summaries by conversationId in order")
    void shouldRetrieveSummaries() throws InterruptedException {
        var convId = ConversationId.random();
        var msg1 = createMessage("Turn one");
        var s1 = summarizer.summarize(convId, List.of(msg1));
        Thread.sleep(1);
        var msg2 = createMessage("Turn two");
        var s2 = summarizer.summarize(convId, List.of(msg2));
        Thread.sleep(1);
        var msg3 = createMessage("Turn three");
        var s3 = summarizer.summarize(convId, List.of(msg3));

        var summaries = summarizer.getSummaries(convId);

        assertEquals(3, summaries.size());
        assertTrue(summaries.get(0).getCreatedAt().isAfter(summaries.get(1).getCreatedAt())
            || summaries.get(0).getCreatedAt().equals(summaries.get(1).getCreatedAt()));
    }

    @Test
    @DisplayName("should restore from summary and recent messages")
    void shouldRestoreFromSummary() {
        var convId = ConversationId.random();
        var messages = List.of(createMessage("Previous conversation content"));
        var summary = summarizer.summarize(convId, messages);
        var recentMessages = List.of(createMessage("Recent message"));

        var restored = summarizer.restoreFromSummary(summary, recentMessages);

        assertTrue(restored.contains("Previous conversation content"));
        assertTrue(restored.contains("Recent message"));
    }
}
