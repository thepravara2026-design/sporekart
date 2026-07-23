# Unified Copilot Workspace — Architecture

**Version:** 1.0.0  
**Last Updated:** 2026-07-23  
**Service Port:** 8103  
**Module:** copilot-workspace-service

---

## Service Boundary

The Copilot Workspace Service runs on **port 8103** and acts as the sole entry point for all copilot interactions. It does not implement business logic — it routes, orchestrates, and manages context. Business intelligence is delegated to the four copilot services (Marketing, Sales, Service, Commerce).

---

## Component Diagram

```
┌──────────────────────────────────────────────────────────────────────────┐
│                     Copilot Workspace Service (8103)                       │
│                                                                            │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                         Workspace Manager                             │  │
│  │  - Session lifecycle (create, get, delete, list)                      │  │
│  │  - Workspace state management                                         │  │
│  │  - Active copilot tracking                                            │  │
│  └────┬────────────────────────────┬─────────────────────────────────────┘  │
│       │                            │                                        │
│  ┌────▼──────────────┐  ┌─────────▼──────────────┐                        │
│  │   Copilot Router   │  │   Collaboration Engine  │                        │
│  │  - Intent detection │  │  - Multi-copilot dispatch│                       │
│  │  - Rule-based routing│  │  - Response merging     │                       │
│  │  - Confidence scoring│  │  - Timeout / error mgmt │                       │
│  │  - Fallback logic   │  │  - Handoff coordination  │                       │
│  └────┬───────────────┘  └────────┬─────────────────┘                        │
│       │                            │                                        │
│  ┌────▼────────────────────────────▼─────────────────────────────────────┐  │
│  │                         Context Broker                                  │  │
│  │  - Context namespaces (workspace, session, user)                       │  │
│  │  - Context get / set / delete / export / import                        │  │
│  │  - Real-time context sync across copilots                              │  │
│  └──────────────────────────────────┬─────────────────────────────────────┘  │
│                                     │                                        │
│  ┌──────────────────────────────────▼─────────────────────────────────────┐  │
│  │                          Memory Broker                                   │  │
│  │  - Shared memory with namespaces                                        │  │
│  │  - TTL-based expiration                                                 │  │
│  │  - Key-value search capabilities                                        │  │
│  └──────────────────────────────────────────────────────────────────────────┘  │
│                                                                            │
└──────────────────────────────────────────────────────────────────────────────┘
          │                            │
          ▼                            ▼
┌──────────────────┐      ┌──────────────────────────┐
│  Copilot Services │      │   Infrastructure          │
│  (4 instances)    │      │   - Redis (memory/context) │
│  - Marketing      │      │   - Event bus (handoffs)   │
│  - Sales          │      └──────────────────────────┘
│  - Service        │
│  - Commerce       │
└──────────────────┘
```

---

## Data Flows

### Chat / Routing Flow

```
User → POST /api/v1/workspace/{id}/chat
         → Workspace Manager (validate session)
         → Copilot Router (detect intent)
         → Route to target copilot
         → Copilot returns response
         → Context Broker (update context)
         → Return ChatResponse
```

### Multi-Copilot Collaboration Flow

```
User → POST /api/v1/workspace/{id}/chat (high-complexity query)
         → Workspace Manager
         → Copilot Router (detects multi-intent)
         → Collaboration Engine
              → Dispatch to Copilot A, Copilot B (parallel)
              → Collect responses (with timeout)
              → Merge responses via strategy
         → Return CollaborationResponse
```

### Handoff Flow

```
User → POST /api/v1/workspace/{id}/switch
         → Workspace Manager (validate target copilot)
         → Context Broker (snapshot current context)
         → Copilot Router (initiate handoff)
         → New copilot loads context
         → Return SwitchCopilotResponse with history
```

---

## Integration with Copilot Services

Each copilot service is abstracted behind a uniform interface:

| Aspect | Detail |
|--------|--------|
| Transport | HTTP (internal) |
| Contract | Shared OpenAPI schema per copilot |
| Discovery | Static config or service registry |
| Auth | Internal service token |
| Health | /health endpoint on each copilot service |

All copilot services expose these standard operations:
- `POST /chat` — single-turn chat
- `POST /chat/stream` — streaming chat
- `GET /health` — health check
