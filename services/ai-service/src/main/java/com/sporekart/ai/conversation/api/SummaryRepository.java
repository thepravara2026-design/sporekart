package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.util.*;

public interface SummaryRepository {
    Optional<Summary> findById(String id);
    Optional<Summary> findByConversationId(ConversationId conversationId);
    List<Summary> findByConversationIdOrderByCreatedAtDesc(ConversationId conversationId);
    Summary save(Summary summary);
    void delete(String id);
    void deleteByConversationId(ConversationId conversationId);
}
