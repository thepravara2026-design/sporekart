package com.sporekart.copilot.streaming;

public interface StreamingHandler {

    void onChunk(StreamingChunk chunk);

    void onComplete();

    void onError(Throwable error);

    void onInterrupt();
}
