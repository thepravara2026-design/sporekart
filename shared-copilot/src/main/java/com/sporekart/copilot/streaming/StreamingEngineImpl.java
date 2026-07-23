package com.sporekart.copilot.streaming;

import com.sporekart.copilot.domain.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StreamingEngineImpl implements StreamingEngine {

    private static final Logger log = LoggerFactory.getLogger(StreamingEngineImpl.class);

    private final ConcurrentMap<SessionId, StreamingHandler> activeStreams = new ConcurrentHashMap<>();

    public StreamingEngineImpl() {
        log.debug("StreamingEngineImpl initialized");
    }

    @Override
    public void start(SessionId sessionId, StreamingHandler handler) {
        log.info("Starting stream for session: {}", sessionId);
        activeStreams.put(sessionId, handler);
    }

    @Override
    public void stream(SessionId sessionId, StreamingChunk chunk) {
        StreamingHandler handler = activeStreams.get(sessionId);
        if (handler == null) {
            log.warn("No active stream for session: {}", sessionId);
            return;
        }
        log.debug("Streaming chunk to session: {}, last={}", sessionId, chunk.last());
        try {
            handler.onChunk(chunk);
        } catch (Exception e) {
            log.error("Error streaming chunk to session {}: {}", sessionId, e.getMessage());
        }
    }

    @Override
    public void complete(SessionId sessionId) {
        StreamingHandler handler = activeStreams.remove(sessionId);
        if (handler == null) {
            log.warn("No active stream to complete for session: {}", sessionId);
            return;
        }
        log.info("Completing stream for session: {}", sessionId);
        try {
            handler.onComplete();
        } catch (Exception e) {
            log.error("Error completing stream for session {}: {}", sessionId, e.getMessage());
        }
    }

    @Override
    public void error(SessionId sessionId, Throwable error) {
        StreamingHandler handler = activeStreams.remove(sessionId);
        if (handler == null) {
            log.warn("No active stream for error on session: {}", sessionId);
            return;
        }
        log.error("Error in stream for session {}: {}", sessionId, error.getMessage());
        try {
            handler.onError(error);
        } catch (Exception e) {
            log.error("Error invoking onError handler for session {}: {}", sessionId, e.getMessage());
        }
    }

    @Override
    public void interrupt(SessionId sessionId) {
        StreamingHandler handler = activeStreams.remove(sessionId);
        if (handler == null) {
            log.warn("No active stream to interrupt for session: {}", sessionId);
            return;
        }
        log.info("Interrupting stream for session: {}", sessionId);
        try {
            handler.onInterrupt();
        } catch (Exception e) {
            log.error("Error interrupting stream for session {}: {}", sessionId, e.getMessage());
        }
    }

    @Override
    public boolean isActive(SessionId sessionId) {
        return activeStreams.containsKey(sessionId);
    }
}
