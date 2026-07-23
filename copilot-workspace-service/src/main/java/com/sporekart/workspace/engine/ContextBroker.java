package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.ContextSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContextBroker {

    private static final Logger log = LoggerFactory.getLogger(ContextBroker.class);

    public enum ContextNamespace {
        USER, CONVERSATION, BUSINESS, KNOWLEDGE, MEMORY
    }

    private final ConcurrentHashMap<String, Map<String, Map<String, Object>>> contexts = new ConcurrentHashMap<>();

    public ContextSnapshot buildContextSnapshot(String sessionId) {
        Map<String, Map<String, Object>> sessionCtx = getOrCreateSessionContext(sessionId);
        return new ContextSnapshot(
            UUID.randomUUID().toString(),
            sessionId,
            (String) sessionCtx.get(ContextNamespace.USER.name()).getOrDefault("userId", "unknown"),
            sessionCtx.get(ContextNamespace.USER.name()),
            sessionCtx.get(ContextNamespace.CONVERSATION.name()),
            sessionCtx.get(ContextNamespace.BUSINESS.name()),
            sessionCtx.get(ContextNamespace.KNOWLEDGE.name()),
            sessionCtx.get(ContextNamespace.MEMORY.name()),
            OffsetDateTime.now()
        );
    }

    public void updateUserContext(String sessionId, Map<String, Object> userContext) {
        updateContext(sessionId, ContextNamespace.USER, userContext);
    }

    public void updateConversationContext(String sessionId, String key, Object value) {
        updateContext(sessionId, ContextNamespace.CONVERSATION, key, value);
    }

    public void updateBusinessContext(String sessionId, String key, Object value) {
        updateContext(sessionId, ContextNamespace.BUSINESS, key, value);
    }

    public void updateKnowledgeContext(String sessionId, String key, Object value) {
        updateContext(sessionId, ContextNamespace.KNOWLEDGE, key, value);
    }

    public Map<String, Object> getContext(String sessionId, String namespace) {
        Map<String, Map<String, Object>> sessionCtx = contexts.get(sessionId);
        if (sessionCtx == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(
            sessionCtx.getOrDefault(namespace.toUpperCase(), Collections.emptyMap())
        );
    }

    public Map<String, Object> getFullContext(String sessionId) {
        Map<String, Map<String, Object>> sessionCtx = contexts.get(sessionId);
        if (sessionCtx == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> flat = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : sessionCtx.entrySet()) {
            flat.put(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(flat);
    }

    public void mergeContext(String sessionId, Map<String, Object> additionalContext) {
        Map<String, Map<String, Object>> sessionCtx = getOrCreateSessionContext(sessionId);
        for (Map.Entry<String, Object> entry : additionalContext.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> namespaceMap = (Map<String, Object>) value;
                sessionCtx.computeIfAbsent(key.toUpperCase(), k -> new ConcurrentHashMap<>()).putAll(namespaceMap);
            }
        }
    }

    public void clearContext(String sessionId) {
        contexts.remove(sessionId);
    }

    public ContextSnapshot exportContext(String sessionId) {
        return buildContextSnapshot(sessionId);
    }

    public void importContext(String sessionId, ContextSnapshot snapshot) {
        Map<String, Map<String, Object>> sessionCtx = getOrCreateSessionContext(sessionId);
        mergeInto(sessionCtx, ContextNamespace.USER, snapshot.userContext());
        mergeInto(sessionCtx, ContextNamespace.CONVERSATION, snapshot.conversationContext());
        mergeInto(sessionCtx, ContextNamespace.BUSINESS, snapshot.businessContext());
        mergeInto(sessionCtx, ContextNamespace.KNOWLEDGE, snapshot.knowledgeContext());
        mergeInto(sessionCtx, ContextNamespace.MEMORY, snapshot.memoryContext());
    }

    public List<String> getActiveContexts() {
        return new ArrayList<>(contexts.keySet());
    }

    private Map<String, Map<String, Object>> getOrCreateSessionContext(String sessionId) {
        return contexts.computeIfAbsent(sessionId, id -> {
            Map<String, Map<String, Object>> nsMap = new ConcurrentHashMap<>();
            for (ContextNamespace ns : ContextNamespace.values()) {
                nsMap.put(ns.name(), new ConcurrentHashMap<>());
            }
            return nsMap;
        });
    }

    private void updateContext(String sessionId, ContextNamespace namespace, String key, Object value) {
        Map<String, Map<String, Object>> sessionCtx = getOrCreateSessionContext(sessionId);
        sessionCtx.get(namespace.name()).put(key, value);
    }

    private void updateContext(String sessionId, ContextNamespace namespace, Map<String, Object> values) {
        Map<String, Map<String, Object>> sessionCtx = getOrCreateSessionContext(sessionId);
        sessionCtx.get(namespace.name()).putAll(values);
    }

    private void mergeInto(Map<String, Map<String, Object>> target, ContextNamespace namespace, Map<String, Object> source) {
        if (source != null) {
            target.get(namespace.name()).putAll(source);
        }
    }
}