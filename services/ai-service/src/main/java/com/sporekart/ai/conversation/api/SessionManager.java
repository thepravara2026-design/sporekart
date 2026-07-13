package com.sporekart.ai.conversation.api;

import com.sporekart.ai.conversation.domain.ConversationSession;
import com.sporekart.ai.conversation.domain.ConversationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionManager {
    ConversationSession createSession(String userId, String title);
    Optional<ConversationSession> getSession(UUID sessionId);
    List<ConversationSession> getUserSessions(String userId);
    ConversationSession updateStatus(UUID sessionId, ConversationStatus status);
    void deleteSession(UUID sessionId);
    ConversationSession suspendSession(UUID sessionId);
    ConversationSession resumeSession(UUID sessionId);
    ConversationSession closeSession(UUID sessionId);
    boolean isSessionActive(UUID sessionId);
}
