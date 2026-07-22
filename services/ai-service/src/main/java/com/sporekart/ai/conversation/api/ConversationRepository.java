package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.util.*;

public interface ConversationRepository {
    Optional<Conversation> findById(ConversationId id);
    List<Conversation> findByUserId(String userId);
    List<Conversation> findByWorkspaceId(WorkspaceId workspaceId);
    List<Conversation> findByStatus(ConversationStatus status);
    List<Conversation> findBySessionId(SessionId sessionId);
    List<Conversation> findAll();
    Conversation save(Conversation conversation);
    void delete(ConversationId id);
    boolean exists(ConversationId id);
}
