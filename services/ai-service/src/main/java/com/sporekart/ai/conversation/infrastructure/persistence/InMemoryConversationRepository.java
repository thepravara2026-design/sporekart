package com.sporekart.ai.conversation.infrastructure.persistence;

import com.sporekart.ai.conversation.api.ConversationRepository;
import com.sporekart.ai.conversation.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryConversationRepository implements ConversationRepository {

    private final ConcurrentHashMap<ConversationId, Conversation> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Conversation> findById(ConversationId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Conversation> findByUserId(String userId) {
        List<Conversation> result = new ArrayList<>();
        for (Conversation c : store.values()) {
            if (userId.equals(c.getUserId())) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public List<Conversation> findByWorkspaceId(WorkspaceId workspaceId) {
        List<Conversation> result = new ArrayList<>();
        for (Conversation c : store.values()) {
            if (workspaceId.equals(c.getWorkspaceId())) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public List<Conversation> findByStatus(ConversationStatus status) {
        List<Conversation> result = new ArrayList<>();
        for (Conversation c : store.values()) {
            if (status == c.getStatus()) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public List<Conversation> findBySessionId(SessionId sessionId) {
        List<Conversation> result = new ArrayList<>();
        for (Conversation c : store.values()) {
            if (sessionId.equals(c.getSessionId())) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public List<Conversation> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Conversation save(Conversation conversation) {
        ConversationId id = conversation.getId();
        if (id == null) {
            id = ConversationId.random();
        }
        store.put(id, conversation);
        return conversation;
    }

    @Override
    public void delete(ConversationId id) {
        store.remove(id);
    }

    @Override
    public boolean exists(ConversationId id) {
        return store.containsKey(id);
    }
}
