package com.sporekart.copilot.streaming;

import com.sporekart.copilot.domain.SessionId;

public interface StreamingEngine {

    void start(SessionId sessionId, StreamingHandler handler);

    void stream(SessionId sessionId, StreamingChunk chunk);

    void complete(SessionId sessionId);

    void error(SessionId sessionId, Throwable error);

    void interrupt(SessionId sessionId);

    boolean isActive(SessionId sessionId);
}
