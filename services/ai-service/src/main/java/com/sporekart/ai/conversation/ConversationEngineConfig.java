package com.sporekart.ai.conversation;

import com.sporekart.ai.conversation.api.*;
import com.sporekart.ai.conversation.application.*;
import com.sporekart.ai.conversation.infrastructure.observability.ConversationMetricsService;
import com.sporekart.ai.conversation.infrastructure.persistence.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversationEngineConfig {

    @Bean
    public ConversationRepository conversationRepository() {
        return new InMemoryConversationRepository();
    }

    @Bean
    public MessageRepository messageRepository() {
        return new InMemoryMessageRepository();
    }

    @Bean
    public MemoryRepository memoryRepository() {
        return new InMemoryMemoryRepository();
    }

    @Bean
    public SessionRepository sessionRepository() {
        return new InMemorySessionRepository();
    }

    @Bean
    public SummaryRepository summaryRepository() {
        return new InMemorySummaryRepository();
    }

    @Bean
    public ConversationService conversationService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository) {
        return new ConversationManager(conversationRepository, messageRepository);
    }

    @Bean
    public MessageEngine messageEngine(
            MessageRepository messageRepository,
            ConversationRepository conversationRepository) {
        return new MessageEngine(messageRepository, conversationRepository);
    }

    @Bean
    public MemoryService memoryService(MemoryRepository memoryRepository) {
        return new MemoryManager(memoryRepository);
    }

    @Bean
    public ContextWindowService contextWindowService() {
        return new ContextWindowManager();
    }

    @Bean
    public SummarizerService summarizerService(
            SummaryRepository summaryRepository,
            MessageRepository messageRepository) {
        return new ConversationSummarizer(summaryRepository, messageRepository);
    }

    @Bean
    public MemoryRetrievalEngine memoryRetrievalEngine(
            MemoryRepository memoryRepository,
            SummaryRepository summaryRepository) {
        return new MemoryRetrievalEngine(memoryRepository, summaryRepository);
    }

    @Bean
    public SessionService sessionService(
            SessionRepository sessionRepository,
            ConversationRepository conversationRepository) {
        return new SessionManager(sessionRepository, conversationRepository);
    }

    @Bean
    public ConversationMetricsService conversationMetricsService() {
        return new ConversationMetricsService();
    }
}
