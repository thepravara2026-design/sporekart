# AI Gateway Architecture

## Overview

The AI Gateway is a middleware layer that sits between AI clients and AI provider APIs. It provides a unified interface for request handling, routing, security, observability, and health management.

## Architecture Diagram

```mermaid
graph TB
    Client[Client Applications]
    LB[Load Balancer]
    GW[AI Gateway]
    PL[Pipeline Executor]
    RT[Provider Router]
    SEC[Security Manager]
    OBS[Observability]
    HLTH[Health Indicator]
    OAI[OpenAI]
    AOA[Azure OpenAI]
    GEM[Gemini]
    CLD[Claude]
    OLL[Ollama]
    BDR[Bédrock]

    Client --> LB
    LB --> GW
    GW --> PL
    GW --> RT
    GW --> SEC
    GW --> OBS
    GW --> HLTH
    PL --> RT
    RT --> OAI
    RT --> AOA
    RT --> GEM
    RT --> CLD
    RT --> OLL
    RT --> BDR
    SEC --> PL
    OBS --> PL

    subgraph "Gateway Core"
        PL
        RT
        SEC
    end

    subgraph "Observability"
        OBS
    end
```

## High-Level Flow

```mermaid
sequenceDiagram
    participant C as Client
    participant GW as AI Gateway
    participant PL as Pipeline
    participant RT as Router
    participant PR as Provider
    participant OBS as Observability

    C->>GW: HTTP Request
    GW->>PL: Execute Pipeline
    PL->>PL: Validate Request
    PL->>PL: Authenticate
    PL->>PL: Authorize
    PL->>PL: Check Quota
    PL->>PL: Rate Limiter
    PL->>RT: Select Provider
    RT-->>PL: Provider Decision
    PL->>PR: Execute Provider Call
    PR-->>PL: Provider Response
    PL->>PL: Post-Process
    PL->>OBS: Record Metrics
    PL->>OBS: Audit Log
    PL-->>GW: Pipeline Result
    GW-->>C: HTTP Response
```

## Component Description

| Component | Responsibility |
|-----------|---------------|
| Pipeline Executor | Orchestrates request through defined stages |
| Provider Router | Resolves which AI provider to use |
| Security Manager | Handles authentication, authorization, tenant isolation |
| Metrics Collector | Records pipeline and provider metrics |
| Tracer | Distributed tracing across pipeline stages |
| Audit Recorder | Captures audit trail for all requests |
| Health Indicator | Monitors gateway and downstream health |
| Gateway Facade | Unified entry point for all gateway operations |

## Directory Structure

```
gateway/
├── pipeline/           # Pipeline framework (stages, interceptors)
├── router/             # Provider routing strategies
├── contract/           # Request/response data contracts
├── security/           # Authentication & authorization hooks
├── exception/          # Exception hierarchy
├── health/             # Health check framework
├── observability/      # Metrics, tracing, logging, audit
├── facade/             # Public API facade
├── config/             # Spring Boot configuration
└── domain/             # Domain models & records
```

## Package Dependencies

```mermaid
graph LR
    FAC[facade] --> PL[pipeline]
    FAC --> RT[router]
    FAC --> SEC[security]
    FAC --> HLTH[health]
    FAC --> OBS[observability]
    PL --> RT
    PL --> SEC
    PL --> OBS
    RT --> domain
    SEC --> domain
    OBS --> domain
    contract --> domain
    exception --> domain
```
