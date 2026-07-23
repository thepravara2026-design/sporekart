# Sprint 30 Release Notes — Enterprise Copilot Framework (RC-1)

> **Version:** 0.2.0-SNAPSHOT  
> **Release Date:** 2026-07-23  
> **Status:** Release Candidate 1  
> **Jira Epic:** SK-450 — Enterprise Copilot Framework

---

## 1. Overview

Sprint 30 introduces the **Enterprise Copilot Framework** — a new platform capability for building, deploying, and managing AI-powered copilot experiences across the SporeKart ecosystem. This release adds a dedicated copilot service, gateway routing, lifecycle management, context assembly, streaming support, and the full SDK for third-party copilot development.

### Key Deliverables

| Component | Description |
|-----------|-------------|
| Copilot Service | New microservice on port 8098 with chat, streaming, session, and management endpoints |
| Gateway Routing | `/api/copilot` route added to the gateway service with circuit breaker, retry, and rate limiting |
| OpenAPI Contract | Complete specification covering 16 endpoints with full request/response schemas |
| SDK | Developer SDK with core interfaces, streaming support, test harness, and Maven plugin |
| Architecture Docs | Comprehensive architecture, API reference, developer guide, and operations runbook |
| Persona System | Pluggable persona management with support, sales, admin, technical, and custom categories |
| Context Assembly | Extensible context assembly from identity, orders, catalog, support, cart, and analytics sources |
| Streaming | SSE-based token streaming for real-time chat experiences |

---

## 2. New Components

### 2.1 Copilot Service (`copilot-service`)

**Port:** 8098  
**Routes:** `/api/copilot/**` (via gateway)

A new Spring Boot microservice that provides the runtime for copilot interactions. It integrates with:
- **AI Service** (port 8088) — LLM model inference
- **Memory Service** (port 8092) — Session persistence
- **Redis** — Session caching and rate limiting
- **Gateway** (port 8080) — Authentication, routing, observability

### 2.2 Copilot SDK (`copilot-sdk`)

Maven modules for building copilot plugins:

| Module | Artifact ID | Purpose |
|--------|-------------|---------|
| `sdk-core` | `copilot-sdk-core` | Core interfaces: `CopilotPlugin`, `CapabilityProvider`, `ToolProvider`, `PersonaProvider`, `ContextProvider` |
| `sdk-streaming` | `copilot-sdk-streaming` | SSE streaming support |
| `sdk-testing` | `copilot-sdk-testing` | Test harnesses and fixtures |
| `sdk-maven-plugin` | `copilot-sdk-maven` | Plugin scaffolding and validation |

### 2.3 OpenAPI Contract

Location: `contracts/openapi/copilot-service.yaml`

Complete OpenAPI 3.0 specification covering all copilot endpoints, schemas, authentication, and error responses.

### 2.4 Documentation Suite

| Document | Location |
|----------|----------|
| Architecture Guide | `docs/copilot-platform/enterprise-copilot-architecture.md` |
| SDK Developer Guide | `docs/copilot-platform/sdk-developer-guide.md` |
| API Reference | `docs/copilot-platform/copilot-api-reference.md` |
| Operations Runbook | `docs/copilot-platform/operations-runbook.md` |
| Release Notes | `docs/copilot-platform/sprint-30-release-notes.md` |

---

## 3. API Changes

### 3.1 New Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/copilot/health` | Service health check |
| `POST` | `/api/copilot/chat` | Send a chat message |
| `POST` | `/api/copilot/stream` | Stream a chat response (SSE) |
| `GET` | `/api/copilot/list` | List registered copilots |
| `GET` | `/api/copilot/capabilities` | List available capabilities |
| `GET` | `/api/copilot/personas` | List available personas |
| `POST` | `/api/copilot/register` | Register a new copilot |
| `POST` | `/api/copilot/context` | Assemble context for a session |
| `GET` | `/api/copilot/{id}` | Get copilot details |
| `GET` | `/api/copilot/{id}/health` | Get copilot health |
| `PUT` | `/api/copilot/{id}/enable` | Enable a copilot |
| `PUT` | `/api/copilot/{id}/disable` | Disable a copilot |
| `POST` | `/api/copilot/session` | Create a session |
| `GET` | `/api/copilot/session/{sessionId}` | Get session details |
| `DELETE` | `/api/copilot/session/{sessionId}` | Delete a session |

### 3.2 Schema Changes

No breaking schema changes to existing platform services. All new schemas are defined within the copilot domain.

---

## 4. Migration Guide

### 4.1 No Breaking Changes

The Enterprise Copilot Framework is additive. No existing services or APIs have been modified. Teams can adopt the copilot framework incrementally.

### 4.2 New Dependencies

Services that want to expose copilot capabilities need:

```xml
<dependency>
    <groupId>com.sporekart.copilot</groupId>
    <artifactId>copilot-sdk-core</artifactId>
    <version>0.2.0-SNAPSHOT</version>
</dependency>
```

### 4.3 Configuration Updates

Add to `application.yml` for services providing capabilities:

```yaml
sporekart:
  copilot:
    capabilities:
      enabled: true
      auto-register: true
```

---

## 5. Known Issues

| Issue ID | Description | Severity | Status | Workaround |
|----------|-------------|----------|--------|------------|
| SK-501 | Copilot sessions do not survive Redis restart | Medium | Open | Redis persistence must be enabled (RDB + AOF) |
| SK-502 | Context assembly for `analytics` source returns degraded status | Low | Open | Analytics source is optional; chat proceeds without it |
| SK-503 | Streaming connector may drop connection on gateway timeout after 120s | Low | Open | Set `spring.cloud.gateway.httpclient.response-timeout` to match streaming timeout |
| SK-504 | Copilot plugin auto-discovery requires classpath scanning | Medium | Open | Explicitly register plugins via configuration if auto-discovery fails |
| SK-505 | Rate limiter key collisions when multiple copilots share the same API client | Low | Open | Configure per-client rate limits in gateway configuration |

---

## 6. Verification Steps

### 6.1 Service Health

```bash
# Verify copilot service is running
curl -X GET http://localhost:8098/api/copilot/health
# Expected: {"status":"UP","version":"0.2.0-SNAPSHOT",...}

# Verify gateway routing
curl -X GET http://localhost:8080/api/copilot/health
# Expected: Same response via gateway
```

### 6.2 End-to-End Chat

```bash
# 1. Get auth token
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "test", "password": "test"}' | jq -r '.token')

# 2. List available copilots
curl -X GET http://localhost:8080/api/copilot/list \
  -H "Authorization: Bearer $TOKEN"

# 3. Send a chat message
curl -X POST http://localhost:8080/api/copilot/chat \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello, copilot!"}'
# Expected: Chat response with message and finishReason
```

### 6.3 Streaming

```bash
curl -X POST http://localhost:8080/api/copilot/stream \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream" \
  -d '{"message": "Tell me a joke"}'
# Expected: SSE events: token, token, ..., done
```

### 6.4 Session Management

```bash
# Create session
SESSION=$(curl -s -X POST http://localhost:8080/api/copilot/session \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"copilotId": "<copilot-id>"}' | jq -r '.sessionId')

# Get session
curl -X GET "http://localhost:8080/api/copilot/session/$SESSION?includeHistory=true" \
  -H "Authorization: Bearer $TOKEN"

# Delete session
curl -X DELETE "http://localhost:8080/api/copilot/session/$SESSION" \
  -H "Authorization: Bearer $TOKEN"
```

### 6.5 Copilot Lifecycle

```bash
# Register a copilot
REG=$(curl -s -X POST http://localhost:8080/api/copilot/register \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "test-copilot", "persona": "support-agent"}')
COPILOT_ID=$(echo $REG | jq -r '.id')

# Disable
curl -X PUT "http://localhost:8080/api/copilot/$COPILOT_ID/disable" \
  -H "Authorization: Bearer $TOKEN"
# Expected: {"status":"INACTIVE"}

# Enable
curl -X PUT "http://localhost:8080/api/copilot/$COPILOT_ID/enable" \
  -H "Authorization: Bearer $TOKEN"
# Expected: {"status":"ACTIVE"}
```

### 6.6 Context Assembly

```bash
curl -X POST http://localhost:8080/api/copilot/context \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "<session-id>",
    "includeSources": ["orders", "profile"]
  }'
# Expected: Context response with user and order data
```

---

## 7. Dependencies

| Dependency | Version | Notes |
|------------|---------|-------|
| Spring Boot | 3.2.x | |
| Spring Cloud Gateway | 4.1.x | |
| Java | 21 | |
| Redis | 7.x | Session store |
| AI Service | 0.2.0+ | LLM model proxy |
| Memory Service | 0.2.0+ | Session persistence |
| Identity Service | 0.2.0+ | JWT auth |

---

## 8. Feedback

Please report issues to the Platform Team channel in Slack or create a Jira ticket under the SK project with the `copilot` label.

**Platform Team Contact:** platform@sporekart.com
