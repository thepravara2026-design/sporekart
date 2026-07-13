# Enterprise AI Conversation Platform Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Module:** ai-service

---

## Overview

The Enterprise AI Conversation Platform provides session management, message management, conversation memory, context building, and streaming foundation for the SporeKart AI Platform. It serves as the entry point for all user-AI interactions, routing through the platform pipeline: Conversation Manager → Memory Manager → Prompt Platform → Knowledge Platform → Semantic Platform → AI Gateway → Provider Framework.

---

## Architecture Principles

1. **Session Isolation** — Every conversation session is isolated with its own context, memory, and message history
2. **Memory Tiering** — Short-term memory (24h TTL) and long-term memory (indefinite) with relevance scoring
3. **Context Fusion** — Context is assembled from conversation history, knowledge base, semantic search, and user input
4. **Streaming Foundation** — Backend supports SSE streaming for real-time response delivery (provider SDK calls excluded)
5. **Caching First** — Sessions and messages are cached in Redis to reduce database load
6. **Event-Driven** — All conversation lifecycle events published to Kafka for downstream consumers

---

## Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────┐
│                         Client Applications                        │
│        Web App │ Android (future) │ iOS (future) │ API Clients     │
└────────────────────────────┬───────────────────────────────────────┘
                             │ REST / SSE
┌────────────────────────────▼───────────────────────────────────────┐
│                     Conversation Controller                         │
│              /api/v1/conversation/* (16 endpoints)                  │
└────────────────────────────┬───────────────────────────────────────┘
                             │
┌────────────────────────────▼───────────────────────────────────────┐
│                      Conversation Manager                           │
│                                                                     │
│  ┌───────────────────┐  ┌──────────────────┐  ┌──────────────────┐ │
│  │  Session Manager  │  │  Message Manager │  │  Memory Manager  │ │
│  │  - CRUD sessions  │  │  - Send/receive  │  │  - STM/LTM       │ │
│  │  - Status mgmt    │  │  - History       │  │  - Relevance     │ │
│  │  - Suspend/resume │  │  - Pagination    │  │  - Expiration    │ │
│  └────────┬──────────┘  └────────┬─────────┘  └────────┬─────────┘ │
│           │                      │                      │           │
│  ┌────────▼──────────────────────▼──────────────────────▼─────────┐ │
│  │                      Context Builder                            │ │
│  │  Assemblies context from: history + knowledge + semantic + user │ │
│  └────────────────────────────────────────────────────────────────-┘ │
└────────────────────────────┬───────────────────────────────────────┘
                             │
                             ▼
                Prompt Platform → Knowledge Platform →
                Semantic Platform → AI Gateway → Provider Framework
```

---

## Component Map

| Module | Package | Purpose |
|--------|---------|---------|
| conversation-api | `com.sporekart.ai.conversation.api` | Port interfaces (SessionManager, MessageService, MemoryManager, ContextBuilder, ConversationStreamService) |
| conversation-domain | `com.sporekart.ai.conversation.domain` | Domain records (ConversationSession, ConversationMessage, MemoryEntry, ContextEntry) and enums (ConversationStatus, MessageRole, MessageStatus, MemoryType) |
| conversation-application | `com.sporekart.ai.conversation.application` | Application services and security |
| conversation-infrastructure | `com.sporekart.ai.conversation.infrastructure` | JPA entities, repositories, Redis cache, Kafka publisher, monitoring |
| conversation-interfaces | `com.sporekart.ai.conversation.interfaces.rest` | REST controller + DTOs |

---

## API Endpoints

| Method | Path | Purpose |
|--------|------|---------|
| POST | `/api/v1/conversation/sessions` | Create session |
| GET | `/api/v1/conversation/sessions/{id}` | Get session |
| GET | `/api/v1/conversation/sessions?userId=` | List user sessions |
| PUT | `/api/v1/conversation/sessions/{id}/suspend` | Suspend session |
| PUT | `/api/v1/conversation/sessions/{id}/resume` | Resume session |
| PUT | `/api/v1/conversation/sessions/{id}/close` | Close session |
| DELETE | `/api/v1/conversation/sessions/{id}` | Delete session |
| POST | `/api/v1/conversation/sessions/{id}/messages` | Send message |
| GET | `/api/v1/conversation/sessions/{id}/messages` | Get messages |
| GET | `/api/v1/conversation/messages/{id}` | Get message |
| DELETE | `/api/v1/conversation/messages/{id}` | Delete message |
| POST | `/api/v1/conversation/sessions/{id}/memories` | Store memory |
| GET | `/api/v1/conversation/sessions/{id}/memories` | Get memories |
| DELETE | `/api/v1/conversation/memories/{id}` | Delete memory |
| GET | `/api/v1/conversation/sessions/{id}/context` | Get context |
| GET | `/api/v1/conversation/sessions/{id}/sources` | Get context sources |
| POST | `/api/v1/conversation/sessions/{id}/context/refresh` | Refresh context |
| GET | `/api/v1/conversation/health` | Health check |

---

## Data Flow

### Message Flow
```
User → REST → Controller → SecurityService → MessageManager → 
  SessionManager (validate) → MessageRepository (persist) → 
  MonitoringService (metrics) → KafkaPublisher (event) → Response
```

### Context Flow
```
ContextBuilder → MessageRepository (history) → ContextRepository (stored) →
  Merge → Rank by weight → Return ContextEntry list
```

---

## Security

- Input sanitization (injection pattern removal)
- Message validation (max 10KB)
- Rate limiting (100 requests/min per user)
- User suspension support
- Session owner verification

## Monitoring

| Metric | Type | Description |
|--------|------|-------------|
| `conversation.session.latency` | Timer | Session operation latency |
| `conversation.message.latency` | Timer | Message operation latency |
| `conversation.message.sent` | Counter | Messages by role |
| `conversation.session.created` | Counter | Sessions created |
| `conversation.session.closed` | Counter | Sessions closed |
| `conversation.cache.hit` | Counter | Cache hits by namespace |
| `conversation.cache.miss` | Counter | Cache misses by namespace |

## Kafka Events

| Event | Topic | Description |
|-------|-------|-------------|
| `SessionCreated` | conversation-events | New session created |
| `SessionClosed` | conversation-events | Session closed |
| `MessageSent` | conversation-events | Message sent |
| `MemoryStored` | conversation-events | Memory stored |
| `ContextRefreshed` | conversation-events | Context refreshed |

## Redis Cache Namespaces

| Prefix | TTL | Description |
|--------|-----|-------------|
| `conversation:session:` | 30 min | Session cache |
| `conversation:message:` | 15 min | Message list cache |
| `conversation:sessions:` | 30 min | User session list |
| `conversation:context:` | 10 min | Context cache |
