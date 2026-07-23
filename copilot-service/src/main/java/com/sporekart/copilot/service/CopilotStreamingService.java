package com.sporekart.copilot.service;

import com.sporekart.copilot.domain.SessionId;
import com.sporekart.copilot.streaming.StreamingChunk;
import com.sporekart.copilot.streaming.StreamingEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CopilotStreamingService {

    private static final Logger log = LoggerFactory.getLogger(CopilotStreamingService.class);

    private final StreamingEngine streamingEngine;
    private final Map<SessionId, SseEmitter> emitters = new ConcurrentHashMap<>();

    public CopilotStreamingService(StreamingEngine streamingEngine) {
        this.streamingEngine = streamingEngine;
    }

    public SseEmitter startStream(SessionId sessionId) {
        var emitter = new SseEmitter(300_000L);
        emitters.put(sessionId, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(sessionId);
            log.debug("Stream completed for session {}", sessionId);
        });
        emitter.onTimeout(() -> {
            emitters.remove(sessionId);
            log.debug("Stream timed out for session {}", sessionId);
        });
        emitter.onError(e -> {
            emitters.remove(sessionId);
            log.error("Stream error for session {}: {}", sessionId, e.getMessage());
        });

        return emitter;
    }

    public void streamChunk(SessionId sessionId, StreamingChunk chunk) {
        var emitter = emitters.get(sessionId);
        if (emitter == null) {
            log.warn("No emitter found for session {}", sessionId);
            return;
        }
        try {
            emitter.send(SseEmitter.event()
                .name("message")
                .data(chunk));
        } catch (IOException e) {
            log.error("Failed to send chunk for session {}: {}", sessionId, e.getMessage());
            emitters.remove(sessionId);
        }
    }

    public void completeStream(SessionId sessionId) {
        var emitter = emitters.remove(sessionId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("complete").data("[DONE]"));
                emitter.complete();
            } catch (IOException e) {
                log.error("Failed to complete stream for session {}: {}", sessionId, e.getMessage());
            }
        }
    }

    public void errorStream(SessionId sessionId, Throwable error) {
        var emitter = emitters.remove(sessionId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("error").data(error.getMessage()));
                emitter.completeWithError(error);
            } catch (IOException e) {
                log.error("Failed to send error for session {}: {}", sessionId, e.getMessage());
            }
        }
    }
}
