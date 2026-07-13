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
- **Memory Management** — Short-term (24h TTL) and long-term memory with relevance scoring
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
