package com.sporekart.ai.conversation.infrastructure.persistence;

import com.sporekart.ai.conversation.api.SessionRepository;
import com.sporekart.ai.conversation.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySessionRepository implements SessionRepository {

    private final ConcurrentHashMap<SessionId, Session> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Session> findById(SessionId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Session> findByUserId(String userId) {
        List<Session> result = new ArrayList<>();
        for (Session s : store.values()) {
            if (userId.equals(s.getUserId())) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public List<Session> findByWorkspaceId(WorkspaceId workspaceId) {
        List<Session> result = new ArrayList<>();
        for (Session s : store.values()) {
            if (workspaceId.equals(s.getWorkspaceId())) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public List<Session> findByStatus(String status) {
        List<Session> result = new ArrayList<>();
        for (Session s : store.values()) {
            if (status.equals(s.getStatus())) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public List<Session> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Session save(Session session) {
        SessionId id = session.getId();
        if (id == null) {
            id = SessionId.random();
        }
        store.put(id, session);
        return session;
    }

    @Override
    public void delete(SessionId id) {
        store.remove(id);
    }

    @Override
    public boolean exists(SessionId id) {
        return store.containsKey(id);
    }
}
