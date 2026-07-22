package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.time.Instant;
import java.util.*;

public interface MessageRepository {
    Optional<Message> findById(MessageId id);
    List<Message> findByConversationId(ConversationId conversationId);
    List<Message> findByConversationIdOrdered(ConversationId conversationId);
    List<Message> findByConversationIdAfter(ConversationId conversationId, Instant after);
    List<Message> findByType(ConversationId conversationId, MessageType type);
    Message save(Message message);
    List<Message> saveAll(List<Message> messages);
    void delete(MessageId id);
    void deleteByConversationId(ConversationId conversationId);
    int countByConversationId(ConversationId conversationId);
    int totalTokensByConversationId(ConversationId conversationId);
}
