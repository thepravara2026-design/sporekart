package com.sporekart.ai.conversation.infrastructure.persistence;

import com.sporekart.ai.conversation.api.MessageRepository;
import com.sporekart.ai.conversation.domain.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMessageRepository implements MessageRepository {

    private final ConcurrentHashMap<MessageId, Message> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Message> findById(MessageId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Message> findByConversationId(ConversationId conversationId) {
        List<Message> result = new ArrayList<>();
        for (Message m : store.values()) {
            if (conversationId.equals(m.getConversationId())) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public List<Message> findByConversationIdOrdered(ConversationId conversationId) {
        List<Message> result = findByConversationId(conversationId);
        result.sort(Comparator.comparing(Message::getCreatedAt));
        return result;
    }

    @Override
    public List<Message> findByConversationIdAfter(ConversationId conversationId, Instant after) {
        List<Message> result = new ArrayList<>();
        for (Message m : store.values()) {
            if (conversationId.equals(m.getConversationId()) && m.getCreatedAt().isAfter(after)) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public List<Message> findByType(ConversationId conversationId, MessageType type) {
        List<Message> result = new ArrayList<>();
        for (Message m : store.values()) {
            if (conversationId.equals(m.getConversationId()) && type == m.getType()) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public Message save(Message message) {
        MessageId id = message.getId();
        if (id == null) {
            id = MessageId.random();
        }
        store.put(id, message);
        return message;
    }

    @Override
    public List<Message> saveAll(List<Message> messages) {
        List<Message> saved = new ArrayList<>(messages.size());
        for (Message m : messages) {
            saved.add(save(m));
        }
        return saved;
    }

    @Override
    public void delete(MessageId id) {
        store.remove(id);
    }

    @Override
    public void deleteByConversationId(ConversationId conversationId) {
        store.entrySet().removeIf(e -> conversationId.equals(e.getValue().getConversationId()));
    }

    @Override
    public int countByConversationId(ConversationId conversationId) {
        int count = 0;
        for (Message m : store.values()) {
            if (conversationId.equals(m.getConversationId())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int totalTokensByConversationId(ConversationId conversationId) {
        int total = 0;
        for (Message m : store.values()) {
            if (conversationId.equals(m.getConversationId())) {
                Integer usage = m.getTokenUsage();
                if (usage != null) {
                    total += usage;
                } else {
                    Integer count = m.getTokenCount();
                    if (count != null) {
                        total += count;
                    }
                }
            }
        }
        return total;
    }
}
