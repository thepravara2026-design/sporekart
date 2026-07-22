package com.sporekart.ai.conversation.infrastructure.persistence;

import com.sporekart.ai.conversation.api.SummaryRepository;
import com.sporekart.ai.conversation.domain.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySummaryRepository implements SummaryRepository {

    private final ConcurrentHashMap<String, Summary> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Summary> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Summary> findByConversationId(ConversationId conversationId) {
        Summary latest = null;
        for (Summary s : store.values()) {
            if (conversationId.equals(s.getConversationId())) {
                if (latest == null || s.getCreatedAt().isAfter(latest.getCreatedAt())) {
                    latest = s;
                }
            }
        }
        return Optional.ofNullable(latest);
    }

    @Override
    public List<Summary> findByConversationIdOrderByCreatedAtDesc(ConversationId conversationId) {
        List<Summary> result = new ArrayList<>();
        for (Summary s : store.values()) {
            if (conversationId.equals(s.getConversationId())) {
                result.add(s);
            }
        }
        result.sort(Comparator.comparing(Summary::getCreatedAt).reversed());
        return result;
    }

    @Override
    public Summary save(Summary summary) {
        String id = summary.getId();
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        store.put(id, summary);
        return summary;
    }

    @Override
    public void delete(String id) {
        store.remove(id);
    }

    @Override
    public void deleteByConversationId(ConversationId conversationId) {
        store.entrySet().removeIf(e -> conversationId.equals(e.getValue().getConversationId()));
    }
}
