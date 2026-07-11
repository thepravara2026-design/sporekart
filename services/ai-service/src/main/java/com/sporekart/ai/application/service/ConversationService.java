package com.sporekart.ai.application.service;

import com.sporekart.ai.common.exception.ConversationLimitException;
import com.sporekart.ai.domain.model.Conversation;
import com.sporekart.ai.domain.model.ConversationMessage;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ConversationService {
    private static final int MAX_CONVERSATIONS = 50;
    private final List<Conversation> conversations = new ArrayList<>();

    public Conversation startConversation(String owner, String context) {
        if (conversations.size() >= MAX_CONVERSATIONS) {
            throw new ConversationLimitException("Conversation limit reached");
        }
        Conversation conversation = new Conversation(UUID.randomUUID(), owner, context);
        conversations.add(conversation);
        return conversation;
    }

    public List<Conversation> listConversations() {
        return new ArrayList<>(conversations);
    }

    public List<ConversationMessage> getHistory(UUID conversationId) {
        return List.of(new ConversationMessage(UUID.randomUUID(), "assistant", "Welcome", OffsetDateTime.now()));
    }

    public void deleteConversation(UUID conversationId) {
        conversations.removeIf(conversation -> conversation.getId().equals(conversationId));
    }
}

