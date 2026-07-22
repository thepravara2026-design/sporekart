package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.util.*;

public interface ConversationService {
    Conversation createConversation(String title, SessionId sessionId, WorkspaceId workspaceId, String userId);
    Conversation getConversation(ConversationId id);
    List<Conversation> listConversations(String userId, WorkspaceId workspaceId, ConversationStatus status);
    Conversation closeConversation(ConversationId id);
    Conversation archiveConversation(ConversationId id);
    Conversation restoreConversation(ConversationId id);
    void deleteConversation(ConversationId id);
    Conversation updateMetadata(ConversationId id, Map<String,String> metadata);
    Conversation addMessage(ConversationId id, Message message);
}
