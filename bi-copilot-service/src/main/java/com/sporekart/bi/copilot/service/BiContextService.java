package com.sporekart.bi.copilot.service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BiContextService {

    private static final Logger log = LoggerFactory.getLogger(BiContextService.class);

    private final Map<String, BiUserContext> contextStore = new ConcurrentHashMap<>();

    public BiContextService() {
        log.info("BiContextService initialized");
    }

    public void storeContext(String sessionId, String key, Object value) {
        BiUserContext ctx = contextStore.computeIfAbsent(sessionId, k -> new BiUserContext(sessionId));
        ctx.attributes().put(key, value);
        log.debug("Stored context key={} for session={}", key, sessionId);
    }

    public Object getContext(String sessionId, String key) {
        BiUserContext ctx = contextStore.get(sessionId);
        return ctx != null ? ctx.attributes().get(key) : null;
    }

    public Map<String, Object> getAllContext(String sessionId) {
        BiUserContext ctx = contextStore.get(sessionId);
        return ctx != null ? Map.copyOf(ctx.attributes()) : Map.of();
    }

    public void clearContext(String sessionId) {
        contextStore.remove(sessionId);
        log.debug("Cleared context for session={}", sessionId);
    }

    public void updateLastActivity(String sessionId) {
        BiUserContext ctx = contextStore.get(sessionId);
        if (ctx != null) {
            contextStore.put(sessionId, new BiUserContext(sessionId, ctx.attributes(), OffsetDateTime.now()));
        }
    }

    private record BiUserContext(
        String sessionId,
        Map<String, Object> attributes,
        OffsetDateTime lastActivity
    ) {
        public BiUserContext(String sessionId) {
            this(sessionId, new ConcurrentHashMap<>(), OffsetDateTime.now());
        }
    }
}
