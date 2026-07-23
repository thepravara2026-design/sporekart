package com.sporekart.copilot.streaming;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record StreamingChunk(
    String id,
    String content,
    boolean last,
    Map<String, Object> metadata
) {
    public StreamingChunk {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(content, "content must not be null");
        metadata = metadata == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(metadata));
    }

    public static StreamingChunk of(String content) {
        return new StreamingChunk(UUID.randomUUID().toString(), content, false, Collections.emptyMap());
    }

    public static StreamingChunk last(String content) {
        return new StreamingChunk(UUID.randomUUID().toString(), content, true, Collections.emptyMap());
    }
}
