package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.MessageRepository;
import com.sporekart.ai.conversation.api.SummarizerService;
import com.sporekart.ai.conversation.api.SummaryRepository;
import com.sporekart.ai.conversation.domain.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ConversationSummarizer implements SummarizerService {
    private static final int MAX_SUMMARY_LENGTH = 500;

    private final SummaryRepository summaryRepository;
    private final MessageRepository messageRepository;

    public ConversationSummarizer(SummaryRepository summaryRepository, MessageRepository messageRepository) {
        this.summaryRepository = summaryRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Summary summarize(ConversationId conversationId, List<Message> messages) {
        var fullContent = messages.stream()
            .map(Message::getContent)
            .filter(Objects::nonNull)
            .collect(Collectors.joining("\n"));

        var originalTokens = fullContent.length() / 4;

        var compressed = fullContent.length() > MAX_SUMMARY_LENGTH
            ? fullContent.substring(0, MAX_SUMMARY_LENGTH) + " ..."
            : fullContent;

        var compressedTokens = compressed.length() / 4;
        var ratio = originalTokens > 0 ? (compressedTokens * 100) / originalTokens : 0;

        var strategy = chooseStrategy(messages.size());

        var summary = new Summary(UUID.randomUUID().toString(), conversationId, compressed, strategy,
            originalTokens, compressedTokens, ratio, Instant.now());
        return summaryRepository.save(summary);
    }

    @Override
    public Summary incrementalSummarize(ConversationId conversationId, Summary previousSummary, List<Message> newMessages) {
        var previousContent = previousSummary.getSummary();
        var newContent = newMessages.stream()
            .map(Message::getContent)
            .filter(Objects::nonNull)
            .collect(Collectors.joining("\n"));

        var combined = previousContent + "\n" + newContent;
        var originalTokens = combined.length() / 4;

        var compressed = combined.length() > MAX_SUMMARY_LENGTH
            ? combined.substring(0, MAX_SUMMARY_LENGTH) + " ..."
            : combined;

        var compressedTokens = compressed.length() / 4;
        var ratio = originalTokens > 0 ? (compressedTokens * 100) / originalTokens : 0;

        var strategy = "llm_truncation";

        var summary = new Summary(UUID.randomUUID().toString(), conversationId, compressed, strategy,
            originalTokens, compressedTokens, ratio, Instant.now());
        return summaryRepository.save(summary);
    }

    @Override
    public List<Summary> getSummaries(ConversationId conversationId) {
        return summaryRepository.findByConversationIdOrderByCreatedAtDesc(conversationId);
    }

    @Override
    public String restoreFromSummary(Summary summary, List<Message> recentMessages) {
        var recentContent = recentMessages.stream()
            .map(Message::getContent)
            .filter(Objects::nonNull)
            .collect(Collectors.joining("\n"));
        return summary.getSummary() + "\n" + recentContent;
    }

    private String chooseStrategy(int messageCount) {
        if (messageCount > 50) return "abstractive";
        if (messageCount > 20) return "llm_truncation";
        return "sliding_window";
    }
}
