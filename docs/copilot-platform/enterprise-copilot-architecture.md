# Enterprise Copilot Architecture

> **Version:** 0.2.0-SNAPSHOT | **Status:** Draft | **Last Updated:** 2026-07-23

---

## 1. Overview and Purpose

The SporeKart Enterprise Copilot Framework provides a unified, extensible platform for building, deploying, and managing AI-powered copilot experiences across the enterprise. It abstracts common patterns — persona management, context assembly, tool execution, streaming, memory, and permission enforcement — so that teams can focus on domain-specific copilot behavior.

### Core Objectives

- **Unified Gateway** — All copilot traffic routes through the existing gateway-service (`/api/copilot`), inheriting authentication, rate limiting, and observability.
- **Pluggable Architecture** — Copilots, capabilities, tools, and personas are first-class pluggable components registered at runtime.
- **Context-Aware** — Automatic assembly of relevant context from order history, product catalog, user profiles, support tickets, and more.
- **Multi-Persona** — Each copilot can adopt different personas (support, sales, admin, technical) with tailored system prompts and tool access.
- **Streaming First** — Full SSE streaming support for real-time chat experiences.
- **Enterprise Ready** — RBAC integration, audit logging, rate limiting, circuit breakers, and distributed tracing.

### Design Tenets

1. **Service boundaries are respected** — The copilot framework orchestrates, it does not implement domain logic.
2. **Everything is a plugin** — Capabilities, tools, personas, and even copilots themselves are registered plugins.
3. **Context is assembled, not guessed** — Explicit context requests with source selection; no implicit data crawling.
4. **Fail gracefully** — Circuit breakers, fallbacks, and degraded modes at every integration point.

---

## 2. System Architecture

```
                                ┌─────────────────────────────────┐
                                │        Client / SDK             │
                                │  (Web, Mobile, API, IDE Plugin) │
                                └──────────────┬──────────────────┘
                                               │
                                               │ HTTPS / WSS
                                               ▼
                    ┌──────────────────────────────────────────────┐
                    │              Gateway Service                 │
                    │         (Port 8080 /api/copilot)             │
                    │  Auth ─▶ Rate Limiter ─▶ Router ─▶ CB/Fallback│
                    └──────────────────────┬───────────────────────┘
                                           │
                                           ▼
                    ┌──────────────────────────────────────────────┐
                    │           Copilot Service                    │
                    │              (Port 8098)                     │
                    │                                              │
                    │  ┌──────────┐ ┌──────────┐ ┌─────────────┐  │
                    │  │  Chat    │ │ Streaming │ │  Session    │  │
                    │  │  Engine  │ │  Engine   │ │  Manager    │  │
                    │  └────┬─────┘ └────┬─────┘ └──────┬──────┘  │
                    │       │            │               │         │
                    │  ┌────▼────────────▼───────────────▼──────┐ │
                    │  │         Core Engine                     │ │
                    │  │  ┌─────────┐ ┌────────┐ ┌──────────┐  │ │
                    │  │  │ Persona │ │Context │ │Capability │  │ │
                    │  │  │ Manager │ │Assembl.│ │  Registry │  │ │
                    │  │  ├─────────┤ ├────────┤ ├──────────┤  │ │
                    │  │  │  Tool   │ │Memory  │ │Permission│  │ │
                    │  │  │  Engine │ │Manager │ │  Enforcer│  │ │
                    │  │  ├─────────┤ ├────────┤ ├──────────┤  │ │
                    │  │  │ Stream  │ │ Event  │ │  Plugin  │  │ │
                    │  │  │ Handler │ │  Bus   │ │  Manager │  │ │
                    │  │  └─────────┘ └────────┘ └──────────┘  │ │
                    │  └────────────────────────────────────────┘ │
                    └──────────────────────┬──────────────────────┘
                                           │
                           ┌───────────────┼───────────────────┐
                           │               │                   │
                           ▼               ▼                   ▼
                    ┌──────────┐   ┌──────────────┐   ┌──────────────┐
                    │  Domain  │   │  AI Service   │   │ Conversation │
                    │ Services │   │  (LLM Proxy)  │   │   Engine     │
                    │ (8081-   │   │  /api/ai      │   │              │
                    │  8097)   │   │  Port 8088    │   │              │
                    └──────────┘   └──────────────┘   └──────────────┘
```

---

## 3. Component Descriptions

### 3.1 Core Engine

The heart of the copilot framework. It orchestrates the complete lifecycle of a copilot interaction:

- Receives requests from the chat/streaming endpoints
- Resolves the target copilot and persona
- Assembles context via the Context Assembler
- Invokes capabilities and tools through the Capability Registry and Tool Engine
- Manages conversation memory
- Enforces permissions and rate limits
- Returns or streams the response

**Lifecycle of a single request:**

```
Request ─▶ Auth Check ─▶ Rate Limit ─▶ Copilot Resolution
    ─▶ Persona Selection ─▶ Context Assembly
    ─▶ Memory Injection ─▶ Tool/Capability Execution
    ─▶ LLM Invocation ─▶ Response Formatting ─▶ Response
```

### 3.2 Persona Manager

Manages the registration, discovery, and runtime application of personas.

| Aspect | Details |
|--------|---------|
| Storage | In-memory registry with optional persistence to the memory service |
| Selection | By copilot configuration, request override, or automatic detection |
| Composition | Personas can extend base personas with additional prompt segments |
| Lifecycle | Create, update, delete, enable, disable at runtime |

Each persona defines:
- **System prompt** — The base instruction set for the LLM
- **Temperature** — Response creativity (0.0–2.0)
- **Max tokens** — Maximum response length
- **Allowed capabilities** — Which domain capabilities this persona can invoke
- **Tool whitelist** — Which tools are available
- **Metadata** — Key-value pairs for extensibility

### 3.3 Context Assembler

Gathers relevant context from across the platform to enrich copilot interactions.

**Context sources:**
- **User profile** — Identity service (name, email, role, preferences)
- **Order history** — Order service (recent orders, status, returns)
- **Product catalog** — Catalog service (product details, pricing)
- **Support tickets** — Support service (open/closed tickets)
- **Cart** — Cart service (current session cart)
- **Analytics** — Analytics service (user behavior, recommendations)
- **Custom** — Any registered context provider plugin

**Assembly strategy:**
1. Request specifies which sources to include (or uses defaults)
2. Each source is queried in parallel via the event bus
3. Results are merged, token-counted, and trimmed to fit the context window
4. Failed sources return degraded context (error flagged, not hard failure)

### 3.4 Capability Registry

A registry of all domain capabilities that copilots can invoke.

| Attribute | Description |
|-----------|-------------|
| `name` | Unique capability identifier (e.g., `order.lookup`) |
| `category` | `query`, `mutation`, `tool`, `integration`, `custom` |
| `inputSchema` | JSON Schema for required inputs |
| `outputSchema` | JSON Schema for expected outputs |
| `cost` | Estimated compute cost for rate limiting |
| `timeout` | Max execution duration |

Capabilities are implemented as plugins and registered at startup or dynamically.

### 3.5 Tool Engine

Executes tools — functions that the LLM can request to call during a conversation. Tools bridge the gap between natural language and system actions.

**Tool lifecycle:**
1. LLM produces a tool call request (function name + arguments)
2. Tool Engine validates the request against the tool schema
3. Executes the tool via the appropriate plugin
4. Returns the result to the LLM for response generation

### 3.6 Memory Manager

Maintains conversation history across sessions.

| Feature | Description |
|---------|-------------|
| Short-term | In-memory for active sessions with TTL |
| Long-term | Persisted via the memory service (port 8092) |
| Summarization | Automatic summarization of long conversations |
| Retrieval | Contextual retrieval for session resume |

### 3.7 Streaming Handler

Implements Server-Sent Events (SSE) for real-time token streaming.

```
Client                     Copilot Service                   LLM Provider
  │                              │                                │
  │── POST /api/copilot/stream ─▶│                                │
  │                              │── context assembly ──────────▶ │
  │                              │◀── context ─────────────────── │
  │                              │── LLM request ───────────────▶ │
  │                              │◀── token stream ────────────── │
  │◀── event: token (loop) ─────│                                │
  │◀── event: done ─────────────│                                │
  │                              │                                │
```

SSE event format:
```
event: token
data: {"token": "Hello", "index": 0, "sessionId": "..."}

event: token
data: {"token": " world", "index": 1, "sessionId": "..."}

event: done
data: {"id": "...", "finishReason": "stop", "usage": {...}}
```

### 3.8 Permission Enforcer

Integrates with the platform RBAC (gateway-level JWT claims) to enforce fine-grained permissions on copilot operations.

| Level | Enforcement |
|-------|-------------|
| Gateway | JWT validation, route-level access (`/api/copilot/**`) |
| Service | Role checks on copilot CRUD operations |
| Capability | Per-capability role requirements |
| Tool | Per-tool role/permission requirements |
| Data | Row-level context filtering based on user roles |

### 3.9 Event Bus

Internal event bus for asynchronous communication between components:

- Context assembly completion events
- Tool execution completion events
- Session lifecycle events
- Copilot registration/deregistration events
- Metrics and audit events

### 3.10 Plugin Manager

Manages the plugin lifecycle for capabilities, tools, context providers, and copilot implementations.

| Operation | Description |
|-----------|-------------|
| Register | Load a plugin class or JAR |
| Configure | Apply configuration properties |
| Start | Initialize the plugin |
| Stop | Gracefully shut down the plugin |
| Reload | Reconfigure without full restart |

---

## 4. Integration Points

### 4.1 Gateway Service (Port 8080)

All copilot traffic routes through `GET/POST/DELETE /api/copilot/**` on the gateway. The gateway handles:

- **Authentication** — JWT validation
- **Rate Limiting** — Per-client copilot request limits
- **Circuit Breaker** — Protects downstream services
- **Request Logging** — Structured audit logs
- **Tracing** — Distributed trace headers propagation

### 4.2 RBAC (Identity Service, Port 8081)

- Copilot management requires `ROLE_ADMIN` or `ROLE_COPILOT_MANAGER`
- Chat/stream endpoints require any authenticated role
- Session management requires the session owner or admin

### 4.3 AI Service (Port 8088)

- LLM proxy for model inference
- Supports GPT-4o, GPT-4o-mini, and custom fine-tuned models
- Configurable per-persona model selection
- Token counting, streaming, and function calling

### 4.4 Conversation Engine

- Long-running conversation orchestration
- Multi-turn dialog management
- State persistence via memory service

### 4.5 Domain Services

The copilot invokes domain capabilities (order lookup, catalog search, etc.) through the event bus or direct HTTP calls to the respective services. All service URLs are managed via gateway service registry.

---

## 5. Data Flows

### 5.1 Chat Request Processing

```
┌────────┐   ┌─────────┐   ┌──────────┐   ┌─────────┐   ┌────────┐   ┌─────────┐
│ Client │   │ Gateway │   │ Copilot  │   │ Context │   │ Memory │   │   AI    │
│        │   │         │   │ Engine   │   │ Assemb. │   │ Manager│   │ Service │
└───┬────┘   └────┬────┘   └────┬─────┘   └────┬────┘   └────┬───┘   └────┬────┘
    │             │              │              │             │            │
    │── POST ────▶│── JWT ──────│              │             │            │
    │  /api/      │   Auth      │              │             │            │
    │  copilot/   │─────────────│──────────────│─────────────│────────────│
    │  chat       │              │              │             │            │
    │             │              │── resolve ──▶│             │            │
    │             │              │   copilot    │             │            │
    │             │              │◀─ persona ───│             │            │
    │             │              │              │             │            │
    │             │              │──── context ──────────────▶│            │
    │             │              │◀─── history ───────────────│            │
    │             │              │              │             │            │
    │             │              │── assemble ─▶│             │            │
    │             │              │◀─ context ───│             │            │
    │             │              │              │             │            │
    │             │              │─── LLM req ───────────────────────────▶│
    │             │              │◀─── response ──────────────────────────│
    │             │              │              │             │            │
    │             │              │── persist ────────────────▶│            │
    │             │              │              │             │            │
    │◀─── response ─────────────│              │             │            │
    │             │              │              │             │            │
```

### 5.2 Streaming Flow

```
Client              Gateway           Copilot Service          AI Service
  │                    │                    │                      │
  │ POST /stream       │                    │                      │
  │───────────────────▶│───────────────────▶│                      │
  │                    │                    │── context assembly   │
  │                    │                    │── session lookup     │
  │                    │                    │── LLM request ──────▶│
  │                    │                    │                      │
  │                    │  SSE: event=token  │◀─── token stream ───│
  │◀───────────────────│◀───────────────────│                      │
  │                    │                    │                      │
  │                    │  SSE: event=token  │◀─── token ──────────│
  │◀───────────────────│◀───────────────────│                      │
  │                    │         ...        │         ...          │
  │                    │                    │                      │
  │                    │  SSE: event=done   │◀─── stream end ─────│
  │◀───────────────────│◀───────────────────│                      │
  │                    │                    │                      │
```

### 5.3 Context Assembly Flow

```
Copilot Engine           Context Assembler          Domain Services
     │                         │                        │
     │── assembleContext() ───▶│                        │
     │                         │── parallel fetch ─────▶│
     │                         │   ┌────────────────┐   │
     │                         │   │ Identity (user) │──▶│
     │                         │   │ Orders (recent) │──▶│
     │                         │   │ Catalog (viewed)│──▶│
     │                         │   │ Support (tickets)│──▶│
     │                         │   │ Cart (current)  │──▶│
     │                         │   └────────────────┘   │
     │                         │◀─── results ──────────│
     │                         │                        │
     │                         │── merge & trim ────────│
     │◀── assembled context ───│                        │
     │                         │                        │
```

---

## 6. Security Architecture

### 6.1 Authentication

- All requests require a JWT bearer token from the Identity Service
- The gateway validates tokens at the entry point
- The copilot service validates tokens for internal operations

### 6.2 Authorization

| Operation | Required Role |
|-----------|---------------|
| List copilots | `ROLE_USER` (own), `ROLE_ADMIN` (all) |
| Chat/Stream | `ROLE_USER` |
| Register copilot | `ROLE_ADMIN`, `ROLE_COPILOT_MANAGER` |
| Enable/Disable copilot | `ROLE_ADMIN` |
| Manage personas | `ROLE_ADMIN` |
| Manage capabilities | `ROLE_ADMIN` |
| Create session | `ROLE_USER` |
| View session | `ROLE_USER` (own), `ROLE_ADMIN` (all) |
| Delete session | `ROLE_USER` (own), `ROLE_ADMIN` (all) |

### 6.3 Data Security

- Context assembly respects data access policies
- User-specific context (orders, tickets) is scoped to the authenticated user
- Admin copilots can access cross-user data with explicit audit logging
- All PII in context is masked based on persona configuration

### 6.4 Audit Logging

Every copilot interaction is logged with:
- Timestamp
- User ID
- Copilot ID
- Persona used
- Capabilities/tools invoked
- Token usage
- Response status

---

## 7. Deployment Architecture

### 7.1 Service Topology

```
┌─────────────────────────────────────────────────────────┐
│                  Kubernetes Cluster                      │
│                                                         │
│  ┌──────────────┐   ┌──────────────┐   ┌──────────────┐│
│  │   Gateway     │   │  Copilot     │   │   Identity   ││
│  │   Service     │   │  Service     │   │   Service    ││
│  │   2 replicas  │   │   3 replicas │   │   2 replicas ││
│  └──────────────┘   └──────────────┘   └──────────────┘│
│                                                         │
│  ┌──────────────┐   ┌──────────────┐   ┌──────────────┐│
│  │   AI Service  │   │  Memory      │   │  Domain      ││
│  │   3 replicas  │   │  Service     │   │  Services    ││
│  │   GPU node    │   │  2 replicas  │   │  varied      ││
│  └──────────────┘   └──────────────┘   └──────────────┘│
│                                                         │
│  ┌──────────────────────────────────────────────────┐   │
│  │              Redis Cluster                        │   │
│  │  (Session cache, rate limiter, event bus)         │   │
│  └──────────────────────────────────────────────────┘   │
│                                                         │
│  ┌──────────────────────────────────────────────────┐   │
│  │              PostgreSQL (via domain services)     │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### 7.2 Resource Requirements

| Component | CPU | Memory | Replicas | Storage |
|-----------|-----|--------|----------|---------|
| Copilot Service | 2 cores | 4 GB | 2–5 | 10 GB ephemeral |
| AI Service | 4 cores | 16 GB | 2–5 | 20 GB ephemeral |
| Memory Service | 1 core | 2 GB | 2 | 50 GB SSD |
| Gateway | 1 core | 2 GB | 2 | 5 GB ephemeral |

### 7.3 Scaling

- **Horizontal scaling**: Stateless copilot service replicas behind a load balancer
- **Vertical scaling**: AI service on GPU nodes for large model inference
- **Session affinity**: Optional, enabled via Redis session store
- **Auto-scaling**: Based on CPU/memory utilization and request queue depth

---

## 8. Future Copilot Extension Guide

### 8.1 Adding a New Copilot Type

1. Implement the copilot plugin interface
2. Register capabilities specific to the domain
3. Define a persona (or extend an existing one)
4. Register tools that the copilot can invoke
5. Register the copilot via `POST /api/copilot/register`
6. Verify via health check and test chat

### 8.2 Adding a New Capability

1. Implement the `CapabilityProvider` interface
2. Define input/output JSON schemas
3. Register with the Capability Registry
4. Add the capability to the desired persona's allowed list
5. Test via the copilot chat endpoint

### 8.3 Adding a New Tool

1. Implement the `ToolProvider` interface
2. Define the tool's function schema (name, description, parameters)
3. Register with the Tool Engine
4. Grant tool access to the relevant personas
5. Validate via a chat request that triggers the tool

### 8.4 Adding a Context Provider

1. Implement the `ContextProvider` interface
2. Implement `fetchContext(contextRequest)` returning assembled context data
3. Register the provider with the Context Assembler
4. The provider key becomes available in context requests

### 8.5 Custom Plugin Development

See the [SDK Developer Guide](./sdk-developer-guide.md) for complete plugin development instructions.

### 8.6 Extension Checklist

- [ ] Plugin implements the appropriate interface
- [ ] Input/output schemas are defined
- [ ] Timeout and cost are configured
- [ ] Permissions are declared
- [ ] Unit and integration tests pass
- [ ] Health check endpoint returns correct status
- [ ] Documentation updated
- [ ] Example usage added to the developer guide

---

## Appendix A: Component Interface Summary

| Component | Interface | Registration | Dependencies |
|-----------|-----------|--------------|--------------|
| Copilot | `CopilotPlugin` | Plugin Manager | Persona, Capabilities, Tools |
| Persona | `PersonaProvider` | Persona Manager | None |
| Capability | `CapabilityProvider` | Capability Registry | Domain Services |
| Tool | `ToolProvider` | Tool Engine | Domain Services |
| Context Provider | `ContextProvider` | Context Assembler | Domain Services |
| Memory Provider | `MemoryProvider` | Memory Manager | Memory Service |
| Permission | `PermissionProvider` | Permission Enforcer | Identity Service |
| Event Handler | `EventHandler` | Event Bus | None |

## Appendix B: Port Allocation

| Service | Port |
|---------|------|
| Gateway Service | 8080 |
| Identity Service | 8081 |
| Catalog Service | 8082 |
| Order Service | 8083 |
| Inventory Service | 8084 |
| Cart Service | 8085 |
| Payment Service | 8086 |
| Notification Service | 8087 |
| AI Service | 8088 |
| Training Service | 8089 |
| Analytics Service | 8090 |
| Admin Service | 8091 |
| Memory Service | 8092 |
| Support Service | 8093 |
| Fulfillment Service | 8094 |
| Risk Service | 8095 |
| Content Service | 8096 |
| Search Service | 8097 |
| **Copilot Service** | **8098** |
