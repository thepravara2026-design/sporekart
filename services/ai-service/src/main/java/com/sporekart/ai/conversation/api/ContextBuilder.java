package com.sporekart.ai.conversation.api;

import com.sporekart.ai.conversation.domain.ContextEntry;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ContextBuilder {
    List<ContextEntry> buildContext(UUID sessionId);
    List<ContextEntry> buildContext(UUID sessionId, String userQuery);
    String buildContextString(UUID sessionId);
    String buildContextString(UUID sessionId, String userQuery);
    Map<String, Double> getContextSources(UUID sessionId);
    void refreshContext(UUID sessionId);
}
