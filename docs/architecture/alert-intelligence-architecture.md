# Alert Intelligence Architecture

## Overview
The Alert Intelligence Engine provides enterprise-wide anomaly detection, business risk assessment, and event timeline tracking. It operates as a standalone microservice (port 8095) within the SporeKart enterprise platform.

## Architecture Principles
- **Hexagonal Architecture** with Ports & Adapters pattern
- **Domain-Driven Design** with rich domain models
- **Stateless**, single-process deployment
- **Fully mocked** — no external dependencies for development
- **Event-driven telemetry** with in-memory metrics

## Service Layer
```
interfaces/ (REST controllers)
    ↓
application/ (services, SDK, engines)
    ↓
domain/ (models, repository ports)
    ↓
infrastructure/ (persistence, cache)
```

## Core Capabilities
| Capability | Engine | Endpoints |
|---|---|---|
| Alert Management | AlertEngine | 10 |
| Risk Assessment | RiskEngine | 5 |
| Anomaly Detection | AnomalyEngine | 4 |
| Event Timeline | TimelineEngine | 3 |
| Telemetry & Cache | — | 4 |

## Domain Model
- **Alert** — Business/operational/security alert with lifecycle (OPEN → ACKNOWLEDGED → RESOLVED)
- **BusinessRisk** — Risk assessment with likelihood, score, mitigation
- **Anomaly** — Detected anomaly with type, deviation, confidence
- **TimelineEvent** — Event entry with type, category, severity, source
- **AlertCache** — TTL-based cache entry with hit tracking

## Technology Stack
- Java 21 + Spring Boot 3.3.3
- Spring Security (Basic Auth + role-based)
- SpringDoc OpenAPI 2.6.0
- JUnit 5 + Mockito (83 tests)
