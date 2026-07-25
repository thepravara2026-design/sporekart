# Alert Intelligence Architecture

## Hexagonal Architecture (Ports & Adapters)

```
┌─────────────────────────────────────────────────────┐
│                   REST Controller                    │
│              (inbound HTTP adapter)                  │
├─────────────────────────────────────────────────────┤
│                   Application Layer                  │
│   ┌──────────┐ ┌──────────┐ ┌────────────────────┐  │
│   │ Services │ │  SDK     │ │     Engines         │  │
│   └──────────┘ └──────────┘ └────────────────────┘  │
├─────────────────────────────────────────────────────┤
│                   Domain Layer                       │
│   ┌──────────┐ ┌──────────┐ ┌────────────────────┐  │
│   │  Models  │ │  Ports   │ │     Enums          │  │
│   └──────────┘ └──────────┘ └────────────────────┘  │
├─────────────────────────────────────────────────────┤
│                 Infrastructure Layer                 │
│   ┌──────────────┐ ┌────────────────────────────┐   │
│   │ Repositories │ │   Cache Service             │   │
│   └──────────────┘ └────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

## Domain Models
- **Alert** — Immutable, UUID-based, static factory `create()`
- **BusinessRisk** — Score 0–100, 10 categories
- **Anomaly** — Confidence 0.0–1.0, expected vs actual
- **TimelineEvent** — Chronological, typed, color-coded

## Data Flow
1. Controller receives HTTP request
2. Service delegates to engine or registry
3. Engine generates domain objects
4. Repository persists in-memory
5. Response returned as JSON

## Security
- Stateless Basic Auth filter
- ADMIN role required for all endpoints
- CSRF disabled
- Permissive CORS
