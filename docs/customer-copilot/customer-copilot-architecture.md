# Customer Copilot Architecture

**Version:** 0.2.0
**Last Updated:** 2026-07-23
**Service:** customer-copilot-service
**Port:** 8099

---

## 1. Overview

The Customer Copilot is the first production copilot built on the **SporeKart Enterprise Copilot Framework** (`shared-copilot` SDK). It is an AI-powered shopping and support assistant that helps customers discover products, track orders, access training, query knowledge, and receive personalized recommendations — all through a natural language chat interface embedded in the SporeKart web application.

As the reference implementation of the Copilot Framework, the Customer Copilot demonstrates how to:

- Register a copilot persona with the `CopilotEngine`
- Define and route capabilities through the `CopilotEngine.processMessage()` pipeline
- Assemble multi-source context (user profile, cart, orders, browsing history)
- Integrate with downstream platform services (Catalog, Order, Training, Knowledge Platform)
- Deliver responses with actionable suggestions

---

## 2. System Context

```
┌─────────────────────────────────────────────────────────────────────┐
│                      SporeKart Enterprise Platform                    │
│                                                                       │
│  ┌──────────────┐   ┌──────────────────┐   ┌──────────────────────┐ │
│  │  Web App      │   │  Admin Dashboard │   │   Mobile App (fut.) │ │
│  │  CopilotPanel │   │  (internal use)  │   │                      │ │
│  └──────┬───────┘   └──────────────────┘   └──────────────────────┘ │
│         │                                                             │
│         │  HTTPS / JWT                                                 │
│         ▼                                                             │
│  ┌──────────────────────────────────────────────────────────────────┐│
│  │                    API Gateway (gateway-service)                  ││
│  │             JWT validation, rate limiting, routing                ││
│  └────┬──────────────┬──────────────┬──────────────┬────────────────┘│
│       │              │              │              │                  │
│       ▼              ▼              ▼              ▼                  │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────────────────┐    │
│  │ Identity │ │ Catalog  │ │ Order    │ │ Customer Copilot     │    │
│  │ Service  │ │ Service  │ │ Service  │ │ (customer-copilot-   │    │
│  │ :8081    │ │ :8083    │ │ :8086    │ │  service :8099)      │    │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┬───────────┘    │
│                                                     │                  │
│            ┌────────────────────────────────────────┼──────────┐      │
│            │              Platform Services          │          │      │
│            │   ┌────────────────┐  ┌─────────────┐  │          │      │
│            │   │ Training       │  │ Knowledge   │  │          │      │
│            │   │ Service        │  │ Platform    │  │          │      │
│            │   │ (training-svc) │  │ (ai-service)│  │          │      │
│            │   └────────────────┘  └─────────────┘  │          │      │
│            │   ┌────────────────┐  ┌─────────────┐  │          │      │
│            │   │ Cart Service   │  │ Copilot     │  │          │      │
│            │   │ (cart-service) │  │ Service     │  │          │      │
│            │   └────────────────┘  │ (fleet mgmt)│  │          │      │
│            │                       └─────────────┘  │          │      │
│            └────────────────────────────────────────┘          │      │
│                                                                       │
│  ┌──────────────────────────────────────────────────────────────────┐│
│  │              Shared-Copilot SDK (shared-copilot)                 ││
│  │  CopilotEngine │ CopilotSDK │ PersonaEngine │ MemoryStore        ││
│  │  CapabilityRegistry │ ToolExecutor │ Suggestion                  ││
│  └──────────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. Component Architecture

### 3.1 Customer Copilot Service (`customer-copilot-service`)

```
┌────────────────────────────────────────────────────────────────────┐
│                    CustomerCopilotController                        │
│              /api/v1/copilot/customer/*                             │
│         REST endpoints for chat, stream, history,                   │
│         recommendations, context, products                          │
└────────────────────────┬───────────────────────────────────────────┘
                         │
┌────────────────────────▼───────────────────────────────────────────┐
│                   CustomerCopilotOrchestrator                        │
│  - create/end/manage sessions                                       │
│  - processMessage(): assemble context → delegate to CopilotEngine  │
│  - handle errors, build ChatResponse                                │
└────┬───────────────┬──────────────────┬──────────────────┬──────────┘
     │               │                  │                  │
┌────▼──────┐  ┌─────▼──────┐   ┌──────▼───────┐   ┌─────▼─────────┐
│Customer   │  │Customer   │   │ Recommendation│   │ Knowledge     │
│ContextSvc │  │Copilot    │   │ Engine         │   │ Integration   │
│           │  │Register   │   │ (multi-        │   │ (RAG queries) │
│- profile  │  │ationSvc   │   │  strategy)     │   │               │
│- cart     │  │           │   │                │   │ - citations   │
│- orders   │  │- register │   │ - personalized │   │ - categories  │
│- history  │  │  persona  │   │ - context-aware│   │ - grounding   │
└────┬──────┘  │- register │   │ - seasonal     │   └──────┬────────┘
     │         │  caps     │   │ - location     │          │
     │         └───────────┘   │ - cross-sell   │          │
     │                         │ - upsell       │          │
     │                         └────────────────┘          │
     │                                                      │
     └──────────────────┬───────────────────────────────────┘
                        │
┌───────────────────────▼────────────────────────────────────┐
│                     CopilotEngine                           │
│            (via shared-copilot SDK)                         │
│                                                             │
│  - CapabilityRegistry (15 capabilities)                     │
│  - PersonaEngine (customer persona)                         │
│  - MemoryStore (conversation memory)                        │
│  - ToolExecutor (invokes registered tools)                  │
│  - IntentRouter (routes to correct capability)              │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 Integration Matrix

| Dependency | Service | Port | Integration Method |
|---|---|---|---|
| Copilot Service | `copilot-service` | - | Shared SDK (`shared-copilot`) |
| Catalog Service | `catalog-service` | 8083 | REST / Feign Client |
| Order Service | `order-service` | 8086 | REST / Feign Client |
| Cart Service | `cart-service` | 8084 | REST / Feign Client |
| Training Service | `training-service` | 8090 | REST / Feign Client |
| Knowledge Platform | `ai-service` (knowledge module) | 8095 | REST / Feign Client |
| Identity Service | `identity-service` | 8081 | JWT validation (gateway) |

### 3.3 Copilot SDK Usage

The `shared-copilot` SDK provides the following core abstractions consumed by the Customer Copilot:

| SDK Component | Usage in Customer Copilot |
|---|---|
| `CopilotEngine` | Central processing engine — `processMessage()`, session lifecycle |
| `CopilotSDK` | Factory — `createDefaultEngine()` |
| `PersonaEngine` | Holds the customer persona definition (tone, style, constraints) |
| `MemoryStore` | Conversation memory management |
| `CapabilityRegistry` | Registration of 15 customer capabilities |
| `ToolExecutor` | Executes capability-specific tools (search, recommend, track, etc.) |
| `IntentRouter` | Routes user intent to the correct capability |
| `Suggestion` | Response suggestion model returned in `ChatResponse` |
| `CopilotType` | Enum value `CUSTOMER` |
| `DefaultPersonas` | Pre-built `customerPersona()` |
| `UserContext` | User identity and metadata passed to engine |
| `PageContext` | Current page context (URL, title, section) |
| `CopilotResponse` | Standard response from `processMessage()` |
| `CopilotStatus` | Session status enum |

---

## 4. Data Flow

### 4.1 Chat Message Flow (Synchronous)

```
User → CopilotPanel
  │
  │  POST /api/v1/copilot/customer/chat
  │  { message, sessionId, pageUrl, pageTitle, section }
  │
  ▼
API Gateway
  │  Validate JWT
  │  Rate limit check
  │
  ▼
CustomerCopilotController.chat()
  │
  ▼
CustomerCopilotOrchestrator.processMessage()
  │
  ├── 1. Get or create session (CopilotEngine.startSession)
  │
  ├── 2. Store user message in conversation history (CustomerContextService)
  │
  ├── 3. Assemble context:
  │     ├── CustomerContextService.assembleContext()
  │     │   ├── Profile (getOrCreateProfile)
  │     │   ├── Cart (getCart)
  │     │   ├── Orders (getOrders)
  │     │   ├── Viewed products
  │     │   └── Journey stage
  │     └── PageContext (from request)
  │
  ├── 4. CopilotEngine.processMessage(sessionId, message, userContext, pageContext)
  │     ├── IntentRouter → identify capability
  │     ├── CapabilityRegistry → find handler
  │     ├── ToolExecutor → execute (may call downstream services)
  │     │   ├── Catalog Service (product search)
  │     │   ├── Order Service (order tracking)
  │     │   ├── Training Service (course lookup)
  │     │   ├── Knowledge Platform (RAG retrieval)
  │     │   └── Recommendation Engine (product recs)
  │     ├── PersonaEngine → format response
  │     └── MemoryStore → update conversation
  │
  ├── 5. Store assistant response in conversation history
  │
  └── 6. Return ChatResponse { sessionId, message, suggestions, context }
```

### 4.2 Streaming Flow

```
User → CopilotPanel
  │
  │  POST /api/v1/copilot/customer/stream
  │  { message, sessionId }
  │
  ▼
CustomerCopilotController.stream()
  │
  ▼
SseEmitter (text/event-stream)
  │
  ├── Event: "context" → { sessionId, pageContext, userInfo, cartInfo }
  ├── Event: "processing" → { status: "analyzing" }
  ├── Event: "token" → { token: "I" }
  ├── Event: "token" → { token: "recommend" }
  ├── Event: "token" → { token: "..." }
  ├── Event: "suggestions" → { suggestions: [...] }
  └── Event: "complete" → { sessionId }
```

### 4.3 Recommendation Flow

```
POST /api/v1/copilot/customer/recommend
  { customerId, category, context, limit }
  │
  ▼
CustomerCopilotController.recommend()
  │
  ▼
RecommendationEngine
  │
  ├── 1. Gather signals:
  │     ├── Purchase history (Order Service)
  │     ├── Preferences (CustomerContextService)
  │     ├── Current context (category, page)
  │     ├── Seasonal calendar
  │     └── Location data
  │
  ├── 2. Score each strategy:
  │     ├── PersonalizedStrategy → score 0-100
  │     ├── ContextAwareStrategy → score 0-100
  │     ├── SeasonalStrategy → score 0-100
  │     ├── LocationAwareStrategy → score 0-100
  │     ├── CrossSellStrategy → score 0-100
  │     └── UpsellStrategy → score 0-100
  │
  ├── 3. Weighted ensemble → final product scores
  │
  ├── 4. Filter (excludeProductIds, availability)
  │
  └── 5. Return RecommendResponse { recommendations, type, explanation }
```

---

## 5. Security Architecture

### 5.1 Authentication & Authorization

```
┌──────────┐     JWT Bearer Token      ┌──────────────┐
│  Client  │ ─────────────────────────→ │   Gateway    │
│          │ ←───────────────────────── │   Service    │
└──────────┘  401 Unauthorized          └──────┬───────┘
                                               │
                                        Validate JWT
                                        (RS256, from Identity Service)
                                               │
                                    ┌──────────▼──────────┐
                                    │  Customer Copilot   │
                                    │  Service            │
                                    │                     │
                                    │  SecurityConfig:     │
                                    │  - /actuator/**     │→ permitAll
                                    │  - /swagger-ui/**   │→ permitAll
                                    │  - /v3/api-docs/**  │→ permitAll
                                    │  - /health          │→ permitAll
                                    │  - /**              │→ authenticated
                                    └─────────────────────┘
```

### 5.2 Security Configuration

- **Stateless sessions** — `SessionCreationPolicy.STATELESS`
- **CSRF disabled** — REST API, no browser-based form login
- **Public endpoints**: health check, Swagger UI, OpenAPI docs
- **All other endpoints**: require valid Bearer JWT
- **JWT validated at gateway** before reaching service
- **Downstream calls**: service-to-service use internal JWT or mTLS

### 5.3 Data Privacy

- Conversation history is scoped to the authenticated user
- Profile data (email, preferences) is read-only from Identity Service
- No PII is logged (emails, addresses masked in logs)
- Cart and order data is retrieved from authoritative services, not cached beyond session

---

## 6. Deployment Architecture

### 6.1 Container

```dockerfile
FROM eclipse-temurin:21-jre
COPY target/customer-copilot-service-*.jar app.jar
EXPOSE 8099
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 6.2 Docker Compose

```yaml
customer-copilot:
  image: sporekart/customer-copilot-service:0.2.0
  ports:
    - "8099:8099"
  environment:
    - SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
    - COPILOT_SERVICE_URL=http://copilot-service:8090
    - CATALOG_SERVICE_URL=http://catalog-service:8083
    - ORDER_SERVICE_URL=http://order-service:8086
    - KNOWLEDGE_SERVICE_URL=http://ai-service:8095
    - TRAINING_SERVICE_URL=http://training-service:8090
  healthcheck:
    test: ["CMD", "curl", "-f", "http://localhost:8099/actuator/health"]
    interval: 30s
    timeout: 10s
    retries: 3
```

### 6.3 Resource Requirements

| Resource | Development | Production |
|---|---|---|
| CPU | 0.5 cores | 2 cores |
| Memory | 512 MB | 2 GB |
| Disk | 100 MB | 500 MB |
| Replicas | 1 | 2-4 |

### 6.4 Dependencies

| Service | Required | Health Check Impact |
|---|---|---|
| Catalog Service | Yes | Recommendations degraded without catalog |
| Order Service | No | Order tracking unavailable |
| Training Service | No | Training queries unavailable |
| Knowledge Platform | No | Knowledge queries degraded |
| Identity Service | Yes | Authentication fails |

---

## 7. Configuration

Configuration is managed via `application.yml` and environment variables:

| Property | Default | Description |
|---|---|---|
| `server.port` | 8099 | HTTP port |
| `customer.copilot.name` | "Customer Copilot" | Copilot display name |
| `customer.copilot.version` | "0.1.0" | Copilot version |
| `customer.copilot.description` | "Enterprise AI Shopping & Support Assistant" | Copilot description |
| `customer.copilot.streaming-enabled` | true | Enable SSE streaming |
| `customer.copilot.max-context-length` | 10 | Max conversation turns in context |
| `customer.copilot.supported-languages` | ["en"] | Supported languages |

---

## 8. Logging & Observability

- **Structured logging**: JSON format via Logback
- **Correlation IDs**: Propagated via `X-Correlation-Id` header
- **Metrics**: Exposed via `/actuator/metrics` (request count, latency, error rate)
- **Health**: `/actuator/health` and `/api/v1/copilot/customer/health`
- **Tracing**: OpenTelemetry integration (future)
