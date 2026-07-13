# Sprint 17 — Part 7: Enterprise AI Conversation Platform

**Date:** 2026-07-12
**Module:** ai-service
**Lead:** Enterprise AI Platform Engineering Team

---

## Objective

Build the Enterprise AI Conversation Platform — session management, message management, conversation memory, context builder, streaming foundation, REST APIs, Flyway migration, Redis caching, Kafka events, security, monitoring, and documentation. No AI content generation in this sprint — provider SDK calls and AI response generation are excluded.

Business modules MUST NOT communicate directly with AI providers; conversation flow: User → Conversation API → Conversation Manager → Memory Manager → Prompt Platform → Knowledge Platform → Semantic Platform → AI Gateway → Provider Framework.

Future Android/iOS must consume same APIs without backend redesign.

---

## Deliverables

### Conversation Platform Modules

| Module | Package | Purpose |
|--------|---------|---------|
| conversation-core | `com.sporekart.ai.conversation.domain` | Domain records, enums |
| conversation-api | `com.sporekart.ai.conversation.api` | Port interfaces (SessionManager, MessageService, MemoryManager, ContextBuilder, ConversationStreamService) |
| conversation-application | `com.sporekart.ai.conversation.application` | Application services, security |
| conversation-infrastructure | `com.sporekart.ai.conversation.infrastructure` | JPA, Redis, Kafka, monitoring |
| conversation-interfaces | `com.sporekart.ai.conversation.interfaces.rest` | REST controller + DTOs |

---

### Domain Model

| Record/Enum | Fields | Purpose |
|-------------|--------|---------|
| `ConversationStatus` | ACTIVE, ARCHIVED, CLOSED | Session lifecycle states |
| `MessageRole` | USER, ASSISTANT, SYSTEM, TOOL, CONTEXT, KNOWLEDGE | Message origin roles |
| `MessageStatus` | PENDING, SENT, DELIVERED, FAILED | Message delivery states |
| `MemoryType` | SHORT_TERM, LONG_TERM | Memory tier classification |
| `ConversationSession` | id, userId, title, status, metadata, createdAt, updatedAt, expiresAt | Session value object |
| `ConversationMessage` | id, sessionId, role, content, metadata, status, createdAt | Message value object |
| `MemoryEntry` | id, sessionId, type, summary, keywords, relevanceScore, createdAt, expiresAt | Memory value object |
| `ContextEntry` | sessionId, source, content, weight | Context source entry |

### API Port Interfaces

| Interface | Key Methods | Purpose |
|-----------|-------------|---------|
| `SessionManager` | createSession, getSession, getUserSessions, updateStatus, deleteSession, suspendSession, resumeSession, closeSession, isSessionActive | Session lifecycle |
| `MessageService` | sendMessage, getMessage, getSessionMessages, updateStatus, deleteMessage, deleteSessionMessages, getMessageCount | Message operations |
| `MemoryManager` | storeMemory, getMemory, getSessionMemories, getRelevantMemories, deleteMemory, updateRelevance | Memory storage |
| `ContextBuilder` | buildContext, buildContextString, getContextSources, refreshContext | Context assembly |
| `ConversationStreamService` | streamResponse, sendMessageStream | Streaming foundation |

### Application Services

| Service | Responsibility |
|---------|---------------|
| `ConversationSessionManager` | Session CRUD, status transitions, soft delete |
| `ConversationMessageManager` | Message send, history, pagination, status |
| `ConversationMemoryManager` | STM (24h TTL) and LTM storage, relevance ranking |
| `ConversationContextBuilder` | Context from history + stored + user query |
| `ConversationStreamServiceImpl` | Streaming foundation (no provider SDK calls) |
| `ConversationSecurityService` | Session access, rate limiting, input sanitization, user suspension |
| `ConversationMonitoringService` | Micrometer metrics, health check, cache ratio |

### JPA Entities (4) + Repositories (4)

| Entity | Table | Repo Methods |
|--------|-------|--------------|
| `ConversationSessionEntity` | conversation_sessions | findByIdAndIsDeletedFalse, findByUserIdAndIsDeletedFalse, findByStatusAndIsDeletedFalse, countByUserIdAndIsDeletedFalse |
| `ConversationMessageEntity` | conversation_messages | findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc, findByIdAndIsDeletedFalse, countBySessionIdAndIsDeletedFalse |
| `ConversationMemoryEntity` | conversation_memories | findBySessionIdAndIsDeletedFalseOrderByCreatedAtDesc, findBySessionIdAndMemoryTypeAndIsDeletedFalseOrderByCreatedAtDesc, findByIdAndIsDeletedFalse |
| `ConversationContextEntity` | conversation_contexts | findBySessionIdAndIsDeletedFalse, findBySessionIdAndSourceAndIsDeletedFalse |

### Flyway Migration V16

8 tables: `conversation_sessions`, `conversation_messages`, `conversation_memories`, `conversation_contexts`, `conversation_session_archive`, `conversation_message_archive`, `conversation_tags`, `conversation_participants`

H2-compatible (TIMESTAMP not TIMESTAMPTZ, TEXT not JSONB, no gen_random_uuid(), no COMMENT ON).

### Redis Cache

| Namespace | TTL | Description |
|-----------|-----|-------------|
| `conversation:session:` | 30 min | Session data |
| `conversation:message:` | 15 min | Message list |
| `conversation:sessions:` | 30 min | User session list |
| `conversation:context:` | 10 min | Context |

### Kafka Events (5 event types on `conversation-events`)

| Event | Key | Payload |
|-------|-----|---------|
| SessionCreated | sessionId | sessionId, userId, timestamp |
| SessionClosed | sessionId | sessionId, userId, timestamp |
| MessageSent | messageId | messageId, sessionId, role, timestamp |
| MemoryStored | memoryId | memoryId, sessionId, memoryType, timestamp |
| ContextRefreshed | sessionId | sessionId, timestamp |

### REST API (18 endpoints under `/api/v1/conversation/*`)

Sessions (7), Messages (4), Memories (3), Context (3), Health (1).

### Security

- Input sanitization (injection pattern: `<>\"';&$`|\\{}()/`)
- Message validation (max 10KB)
- Rate limiting (100 req/min/user)
- User suspension
- Session owner verification

### Monitoring (7 Micrometer metrics)

- `conversation.session.latency` (Timer)
- `conversation.message.latency` (Timer)
- `conversation.message.sent` (Counter, role tag)
- `conversation.session.created` (Counter)
- `conversation.session.closed` (Counter)
- `conversation.cache.hit` (Counter, cache tag)
- `conversation.cache.miss` (Counter, cache tag)

### Feature Flags

| Flag | Default | Purpose |
|------|---------|---------|
| AI_CONVERSATION_ENABLED | true | Master toggle |
| AI_CONVERSATION_CACHING | true | Redis caching |
| AI_CONVERSATION_AUDIT | true | Kafka events |
| AI_CONVERSATION_SESSION | true | Session management |
| AI_CONVERSATION_MEMORY | true | Memory management |
| AI_CONVERSATION_STREAMING | true | Streaming |
| AI_CONVERSATION_RATE_LIMIT | true | Rate limiting |
| AI_CONVERSATION_MONITORING | true | Metrics |

### Modified Files

- `FeatureFlagName.java` — Added 8 conversation flags
- `AiFeatureFlagProperties.java` — Added 8 conversation properties with getters/setters
- `KafkaConfig.java` — Added `conversationEventsTopic()` bean
- `SecurityConfig.java` — Permitted `/api/v1/conversation/**` endpoints
- `application.yml` — Added conversation feature flags, module config

### Test Classes (12, ~100+ tests)

| Test Class | Type | Coverage |
|-----------|------|----------|
| `ConversationSessionManagerTest` | Unit (Mockito) | Session CRUD, status transitions, soft delete, exceptions |
| `ConversationMessageManagerTest` | Unit (Mockito) | Send, history, pagination, status, delete |
| `ConversationMemoryManagerTest` | Unit (Mockito) | Store, retrieve, relevance, delete |
| `ConversationContextBuilderTest` | Unit (Mockito) | Context assembly, sources, refresh |
| `ConversationSecurityServiceTest` | Unit (plain) | Access control, rate limit, sanitization, validation |
| `ConversationMonitoringServiceTest` | Unit (Mockito+Micrometer) | Metrics, latency, cache ratio, health |
| `ConversationSessionRepositoryTest` | DataJpaTest | CRUD, soft delete, custom queries |
| `ConversationMessageRepositoryTest` | DataJpaTest | CRUD, soft delete, ordering |
| `ConversationMemoryRepositoryTest` | DataJpaTest | CRUD, type filtering, soft delete |
| `ConversationControllerTest` | MockMvc | All 18 endpoints, success + error cases |
| `ConversationRedisCacheServiceTest` | Unit (Mockito) | All cache operations, failure handling |
| `ConversationKafkaEventPublisherTest` | Unit (Mockito) | All 5 event types, failure handling |

---

## Conversation Flow

```
User → REST API → ConversationSecurityService (validate, sanitize, rate limit) →
  ConversationMonitoringService (record latency) →
  SessionManager (validate session) →
  MessageManager (persist message) →
  ContextBuilder (assemble context from history + stored + user query) →
  KafkaPublisher (emit event) →
  MonitoringService (record metrics) →
  Response
```

---

## Database Migration

**File:** `V16__sprint17_conversation.sql`

8 tables with 11 indexes. H2-compatible for dev/test, PostgreSQL for production.
