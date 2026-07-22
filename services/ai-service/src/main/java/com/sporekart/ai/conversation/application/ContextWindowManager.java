package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.ContextWindowService;
import com.sporekart.ai.conversation.domain.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContextWindowManager implements ContextWindowService {
    private static final int DEFAULT_MAX_TOKENS = 4096;
    private static final int KEEP_LAST_N = 20;

    @Override
    public ContextWindow buildWindow(ConversationId conversationId, List<Message> messages) {
        var tokens = estimateTokens(messages);
        return new ContextWindow(conversationId.toString(), new ArrayList<>(messages), tokens, DEFAULT_MAX_TOKENS,
            0, new ArrayList<>(), null);
    }

    @Override
    public ContextWindow compress(ContextWindow window) {
        var messages = window.getActiveMessages();
        Message systemMessage = null;
        List<Message> nonSystem = new ArrayList<>();
        for (var msg : messages) {
            if ("system".equals(msg.getRole()) && systemMessage == null) {
                systemMessage = msg;
            } else {
                nonSystem.add(msg);
            }
        }
        var keep = Math.min(KEEP_LAST_N, nonSystem.size());
        var tail = nonSystem.subList(nonSystem.size() - keep, nonSystem.size());
        var compressed = new ArrayList<Message>();
        if (systemMessage != null) compressed.add(systemMessage);
        compressed.addAll(tail);

        var strategies = new ArrayList<>(window.getStrategies());
        strategies.add("sliding_window");

        return new ContextWindow(window.getConversationId(), compressed, estimateTokens(compressed),
            window.getMaxTokens(), window.getCompressionCount() + 1, strategies, Instant.now());
    }

    @Override
    public ContextWindow trim(ContextWindow window, int maxTokens) {
        var messages = new ArrayList<>(window.getActiveMessages());
        Message systemMessage = null;
        var iterator = messages.iterator();
        while (iterator.hasNext()) {
            var msg = iterator.next();
            if ("system".equals(msg.getRole()) && systemMessage == null) {
                systemMessage = msg;
                iterator.remove();
            }
        }

        while (estimateTokens(messages) > maxTokens && !messages.isEmpty()) {
            messages.removeFirst();
        }
        var result = new ArrayList<Message>();
        if (systemMessage != null) result.add(systemMessage);
        result.addAll(messages);

        return new ContextWindow(window.getConversationId(), result, estimateTokens(result),
            window.getMaxTokens(), window.getCompressionCount(), window.getStrategies(), window.getLastCompressedAt());
    }

    @Override
    public ContextWindow prioritize(ContextWindow window, List<String> priorityKeys) {
        var messages = window.getActiveMessages();
        var priority = new ArrayList<Message>();
        var rest = new ArrayList<Message>();
        for (var msg : messages) {
            var content = msg.getContent();
            var match = priorityKeys.stream().anyMatch(k -> content != null && content.contains(k));
            (match ? priority : rest).add(msg);
        }
        priority.addAll(rest);

        var strategies = new ArrayList<>(window.getStrategies());
        strategies.add("priority_ordered");

        return new ContextWindow(window.getConversationId(), priority, estimateTokens(priority),
            window.getMaxTokens(), window.getCompressionCount(), strategies, window.getLastCompressedAt());
    }

    @Override
    public int estimateTokens(List<Message> messages) {
        return messages.stream()
            .mapToInt(m -> {
                var content = m.getContent();
                return content != null ? (content.length() / 4 + 3) : 3;
            })
            .sum();
    }

    @Override
    public boolean needsCompression(ContextWindow window) {
        return window.getTotalTokens() > window.getMaxTokens() * 0.8;
    }

    public String compressContent(String content, int maxLength) {
        if (content == null || content.length() <= maxLength) return content;
        return content.substring(0, maxLength) + "...";
    }
}
