# Package Architecture

## Repository Structure

```
sporekart/
├── shared-platform/      # Shared enterprise platform library
│   ├── error/            # Exception hierarchy + handlers
│   ├── api/              # Standard API responses
│   ├── validation/       # Validation framework
│   ├── logging/          # Logging filters and loggers
│   ├── util/             # Utility classes
│   ├── mapping/          # DTO mapping interface
│   ├── security/         # Shared security config
│   └── config/           # Auto-configuration
├── shared-events/        # Enterprise event backbone
├── services/
│   ├── cart-service/     # Reference implementation
│   ├── order-service/
│   ├── payment-service/
│   └── ... (15 total)
├── docs/                 # Documentation
└── [copilot services]/
```

## Dependency Graph

```
cart-service ──→ shared-platform ──→ Spring Boot
             ──→ shared-events
order-service ──→ shared-platform
             ──→ shared-events (future)
...and so on for all services
```

## Layer Isolation

```
┌──────────────────────────────────┐
│        interfaces/rest/         │  ← Controllers only
├──────────────────────────────────┤
│       application/service/      │  ← Business orchestration
├──────────────────────────────────┤
│         application/dto/        │  ← Request/Response records
├──────────────────────────────────┤
│          domain/model/          │  ← Pure domain (no Spring deps)
├──────────────────────────────────┤
│        domain/repository/       │  ← Port interfaces
├──────────────────────────────────┤
│    infrastructure/persistence/  │  ← Adapters implementing ports
├──────────────────────────────────┤
│             config/             │  ← Spring configuration
└──────────────────────────────────┘
```

Each layer depends only on the layer below. Domain layer has zero framework dependencies.
