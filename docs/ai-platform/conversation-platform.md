# AI Conversation Platform

**Module:** ai-service
**Package:** `com.sporekart.ai.conversation`

---

## Overview

The Conversation Platform manages the full lifecycle of user-AI conversations including session management, message history, conversation memory, and context assembly. It serves as the entry point for all user interactions, routing through the platform pipeline to deliver context-aware responses.

---

## Key Capabilities

- **Session Management** — Create, suspend, resume, close, and delete conversation sessions
- **Message Management** — Send/receive messages, paginated history, status tracking
- **Memory Management** — Six-layer memory (IMMEDIATE→CONVERSATION→SESSION→WORKSPACE→BUSINESS→LONG_TERM) with TTL, promotion, and cross-layer querying
- **Context Window Management** — Build, compress (sliding window at 80% maxTokens), trim, and prioritize context windows
- **Session Lifecycle** — Full lifecycle (Created→Active→Archived→Closed→Deleted) with token usage tracking
- **Observability** — 8 atomic counters for conversations, messages, memory retrievals, summarizations, sessions, and compressions
- **Memory Retrieval Engine** — Cross-layer search with content query across user, workspace, and conversation scopes
- **Context Assembly** — Fuses conversation history, stored context, and user queries
- **Streaming Foundation** — Backend support for SSE streaming (provider SDK calls excluded)
- **Security** — Input sanitization, rate limiting, user suspension, owner verification
- **Monitoring** — 7 Micrometer metrics (latency, counters, cache ratio)

---

## Architecture

```
User → Conversation API → Security → Session Manager → Message Manager →
  Context Builder → Memory Manager → Kafka Event → Monitoring → Response
```

---

## Multi-Layer Memory

The platform supports six memory layers:

| Layer | Scope | TTL | Promotion |
|-------|-------|-----|-----------|
| IMMEDIATE | Current turn | None | Manual |
| CONVERSATION | Single conversation | 24h | Automatic |
| SESSION | Multi-turn session | 7d | Automatic |
| WORKSPACE | Workspace-wide | 30d | Manual |
| BUSINESS | Cross-workspace | 90d | Manual |
| LONG_TERM | Indefinite | None | Manual |

The `MemoryManager` provides: store (with/without TTL), retrieve by key+layer, retrieve by layer, retrieve by layer+workspace, query by content, clear, and promote.

## Context Window Management

The `ContextWindowManager` builds context windows from active messages, compresses via sliding window (>80% maxTokens triggers compression), trims to token limits, and prioritizes by recency and relevance. Token estimation uses `chars/4 + 3`.

## Session Lifecycle

```
Created → Active → Archived (suspend) → Active (resume) → Closed → Deleted
```

Each `Session` tracks `tokenUsage`, `messageCount`, `lastActiveAt`, `startedAt`, and `endedAt`.

## Summarization

The `ConversationSummarizer` compresses conversation history into `Summary` entities and restores context from summaries for continued conversations.

## Observability

`ConversationMetricsService` maintains atomic counters: activeConversations, totalMessages, totalTokenUsage, totalMemoryRetrievals (with avg latency), totalSummarizations (with avg latency), activeSessions, totalContextCompressions. Exposed via `getMetrics()` and `reset()`.

## Memory Retrieval Engine

The `MemoryRetrievalEngine` enables cross-layer search: retrieve by user, by workspace, or query by content across layers, returning relevant entries with context.

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

## Events (Kafka)

- `SessionCreated`, `SessionClosed`, `MessageSent`, `MemoryStored`, `ContextRefreshed`

## Caching (Redis)

- Session (30m), Messages (15m), Session List (30m), Context (10m)
