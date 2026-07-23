package com.sporekart.bi.copilot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BiContextService {

    private static final Logger log = LoggerFactory.getLogger(BiContextService.class);
    private static final int MAX_ENTRIES_PER_SESSION = 100;

    private final ConcurrentHashMap<String, LinkedHashMap<String, ContextEntry>> sessionContexts = new ConcurrentHashMap<>();

    public void put(String sessionId, String key, Object value) {
        var context = sessionContexts.computeIfAbsent(sessionId, k -> new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, ContextEntry> eldest) {
                return size() > MAX_ENTRIES_PER_SESSION;
            }
        });
        context.put(key, new ContextEntry(key, value, OffsetDateTime.now()));
        log.debug("Context updated for session {}: {}={}", sessionId, key, value);
    }

    public Object get(String sessionId, String key) {
        var context = sessionContexts.get(sessionId);
        if (context == null) return null;
        var entry = context.get(key);
        return entry != null ? entry.value() : null;
    }

    public Map<String, Object> getAll(String sessionId) {
        var context = sessionContexts.get(sessionId);
        if (context == null) return Map.of();
        return context.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().value()));
    }

    public List<ContextEntry> getEntries(String sessionId) {
        var context = sessionContexts.get(sessionId);
        if (context == null) return List.of();
        return List.copyOf(context.values());
    }

    public void clear(String sessionId) {
        sessionContexts.remove(sessionId);
        log.debug("Context cleared for session {}", sessionId);
    }

    public void remove(String sessionId, String key) {
        var context = sessionContexts.get(sessionId);
        if (context != null) {
            context.remove(key);
            log.debug("Context entry removed for session {}: {}", sessionId, key);
        }
    }

    public int size(String sessionId) {
        var context = sessionContexts.get(sessionId);
        return context != null ? context.size() : 0;
    }

    public record ContextEntry(String key, Object value, OffsetDateTime timestamp) {}
}
